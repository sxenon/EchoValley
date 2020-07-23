package com.sxenon.echovalley.arch.viewmodule.search;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sxenon.echovalley.arch.Action1;
import com.sxenon.echovalley.arch.Func1;
import com.sxenon.echovalley.arch.util.Preconditions;

public class BaseSearchViewModule implements ISearchViewModule {
    private final EditText mEditText;
    private Func1<CharSequence, Boolean> mSearchAction;

    public BaseSearchViewModule(@NonNull EditText editText, @NonNull Func1<CharSequence, Boolean> searchAction) {
        this(editText, searchAction, null, null);
    }

    public BaseSearchViewModule(@NonNull EditText editText, @NonNull Func1<CharSequence, Boolean> searchAction, @Nullable View cancelView, @Nullable final Action1<View> onCancel) {
        mEditText = editText;
        mSearchAction = searchAction;

        //IME_ACTION_SEARCH：work when
        mEditText.setInputType(EditorInfo.TYPE_CLASS_TEXT);//or android:singleLine="true" or android:inputType="text"
        mEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return actionId == EditorInfo.IME_ACTION_SEARCH && performSearch();
            }
        });

        if (cancelView != null) {
            cancelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Preconditions.checkNotNull(onCancel,"").call(v);
                }
            });
        }
    }

    @Override
    public void setKeyword(CharSequence keyword) {
        mEditText.setText(keyword);
    }

    public boolean performSearch() {
        return mSearchAction.call(mEditText.getText().toString());
    }

}
