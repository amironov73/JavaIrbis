package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;

/**
 * TRE-file line.
 */
public final class TreeNode {

    /**
     * Children.
     */
    public final Collection<TreeNode> children = new LinkedList<>();

    //=========================================================================

    /**
     * Node value.
     */
    public String value;

    //=========================================================================

    /**
     * Default constructor.
     */
    public TreeNode() {
    }

    /**
     * Initializing value.
     * @param value Initial value for the node.
     */
    public TreeNode(@NotNull String value) {
        this.value = value;
    }

    TreeNode(@NotNull String value, int level) {
        this.value = value;
        this.level = level;
    }

    //=========================================================================

    int level;

    //=========================================================================
}
