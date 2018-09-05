package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedList;

/**
 * TRE-file line.
 */
@SuppressWarnings("WeakerAccess")
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

    public TreeNode add(@NotNull String name) {
        TreeNode result = new TreeNode(name);
        children.add(result);

        return result;
    }

    public void write(@NotNull PrintWriter writer, int level) {
        for (int i = 0; i < level; i++) {
            writer.print(TreeFile.INDENT);
        }
        writer.println(value);
        for (TreeNode child: children) {
            child.write(writer, level+1);
        }
    }
}
