package com.sxenon.echovalley.arch.adapter.rv;

@SuppressWarnings("rawtypes")
public class RecyclerViewItemViewTypeEntity {
    private final int resourceId;
    private final Class<? extends RecyclerViewViewHolder> viewHolderClass;

    public RecyclerViewItemViewTypeEntity(int resourceId, Class<? extends RecyclerViewViewHolder> viewHolderClass) {
        this.resourceId = resourceId;
        this.viewHolderClass = viewHolderClass;
    }

    public int getResourceId() {
        return resourceId;
    }

    public Class<? extends RecyclerViewViewHolder> getViewHolderClass() {
        return viewHolderClass;
    }
}
