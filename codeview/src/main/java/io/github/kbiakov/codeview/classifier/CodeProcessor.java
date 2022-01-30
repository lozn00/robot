package io.github.kbiakov.codeview.classifier;
import android.content.Context;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CodeProcessor {

    private static final String TAG = "CodeClassifier";

    private static volatile CodeProcessor sInstance;
    private static Future<CodeProcessor> sTrainingTaskFuture;

    public static CodeProcessor getInstance(Context context) {
        if (notInstanceAvailable()) {
            synchronized (CodeProcessor.class) {
                if (notInstanceAvailable()) {
                    sInstance = new CodeProcessor(context);
                }
            }
        }
        return sInstance;
    }

    /**
     * Private (and only one) constructor.
     * <p>
     * Code processor creation instantiate code classifier training task.
     *
     * @param context Context
     */
    private CodeProcessor(Context context) {
        io.github.kbiakov.codeview.classifier.CodeClassifier1.INSTANCE.train(context);
    }

    /**
     * Code processor should be created ones at start. But processor creation
     * is not guarantee that classifier is available. Not trained classifier
     * is not ready to use & must be trained soon as possible.
     * <p>
     * The main cases why code processor is not available is:
     * 1) processor is not created yet & classifier not trained
     * 2) processor created, but occurs an error on classifier train
     * 3) processor created, classifier start train, but not finished
     * <p>
     * (3rd case is ok, it's temporary unavailability & awaiting for training)
     * <p>
     * In 3rd case, user awaiting for train accomplish to get code processor
     * and then take classifier to perform language classifying (see below).
     *
     * @return Flag indicates that classifier instance is not available.
     */
    private static boolean notInstanceAvailable() {
        if (sInstance == null) {
            if (!sTrainingTaskFuture.isDone()) {
                try {
                    sInstance = sTrainingTaskFuture.get();
                    return false;
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * If training task future is exists, then classifier was started
     * to train or already trained & classifier is ready to use.
     *
     * @return If classifier was trained.
     */
    public boolean isTrained() {
        return sTrainingTaskFuture != null;
    }

    /**
     * Start point for apps that use code classifying. Called ones at app start.
     * It creates training task for code classifier.
     *
     * @param context Context
     */
    public static void init(Context context) {
        if (sInstance == null) {
            final ExecutorService service = Executors.newSingleThreadExecutor();
            sTrainingTaskFuture = service.submit(new TrainingTask(context));
        } else {
            throw new IllegalStateException("Attempt to train code classifier twice.\n" +
                    "It should be initialized once at start to make train asynchronously.");
        }
    }

    /**
     * Creates code snippet language classifying task.
     *
     * @param snippet Code snippet to classify.
     * @return Classified language wrapped in Future.
     */
    public Future<String> classify(String snippet) {
        final ExecutorService service = Executors.newSingleThreadExecutor();
        return service.submit(new ClassifyingTask(snippet));
    }

    /**
     * @author Kirill Biakov
     * @class TrainingTask
     * <p>
     * Classifier training task.
     */
    private static class TrainingTask implements Callable<CodeProcessor> {
        private Context context;

        public TrainingTask(Context context) {
            this.context = context;
        }

        @Override
        public CodeProcessor call() {
            return new CodeProcessor(context);
        }
    }

    /**
     * @author Kirill Biakov
     * @class ClassifyingTask
     * <p>
     * Language classifying task for presented code snippet.
     */
    private static class ClassifyingTask implements Callable<String> {
        private String snippet;

        public ClassifyingTask(String snippet) {
            this.snippet = snippet;
        }

        @Override
        public String call() {
            return io.github.kbiakov.codeview.classifier.CodeClassifier1.INSTANCE.classify(snippet);
        }
    }
}
