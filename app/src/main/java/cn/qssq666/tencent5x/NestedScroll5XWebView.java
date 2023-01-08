/*
 * Copyright (C) 2016 Tobias Rohloff
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.qssq666.tencent5x;

import android.content.Context;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebView;

import java.util.Map;

public class NestedScroll5XWebView  extends WebView  {


    public NestedScroll5XWebView(Context context) {
        super(context);
    }

    public NestedScroll5XWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public NestedScroll5XWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public NestedScroll5XWebView(Context context, AttributeSet attributeSet, int i, boolean b) {
        super(context, attributeSet, i, b);
    }

    public NestedScroll5XWebView(Context context, AttributeSet attributeSet, int i, Map<String, Object> map, boolean b) {
        super(context, attributeSet, i, map, b);
    }
}