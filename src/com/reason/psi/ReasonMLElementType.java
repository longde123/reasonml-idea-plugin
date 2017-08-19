package com.reason.psi;

import com.intellij.psi.tree.IElementType;
import com.reason.lang.RmlLanguage;
import org.jetbrains.annotations.*;

public class ReasonMLElementType extends IElementType {
    public ReasonMLElementType(@NotNull @NonNls String debugName) {
        super(debugName, RmlLanguage.INSTANCE);
    }
}
