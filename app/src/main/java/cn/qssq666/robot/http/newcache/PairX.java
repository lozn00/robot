package cn.qssq666.robot.http.newcache;

public  class PairX<First, Second> {
        public First first;
        public Second second;

    public PairX() {
    }

    public PairX(First first, Second second) {
            this.first = first;
            this.second = second;
        }

    public static<First,Second> PairX<First, Second> create(First a, Second b) {
        return new PairX<>(a,b);
    }
}