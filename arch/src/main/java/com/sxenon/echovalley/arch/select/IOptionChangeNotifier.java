package com.sxenon.echovalley.arch.select;

import java.util.List;

public interface IOptionChangeNotifier {
    void onOptionRemoved(int position);

    void onSelectedOptionsRemoved(List<Boolean> selectedFlags);

    void notifySelectChange(List<Boolean> oldSelectedFlags, List<Boolean> newSelectedFlags);
}
