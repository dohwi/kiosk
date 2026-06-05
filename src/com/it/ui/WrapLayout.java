package com.it.ui;

import java.awt.*;

public class WrapLayout extends FlowLayout {

    public WrapLayout() {
        super(FlowLayout.LEFT, 12, 12);
    }

    public WrapLayout(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        return layoutSize(target, true);
    }

    @Override
    public Dimension minimumLayoutSize(Container target) {
        Dimension minimum = layoutSize(target, false);
        minimum.width -= (getHgap() + 1);
        return minimum;
    }

    private Dimension layoutSize(Container target, boolean preferred) {
        synchronized (target.getTreeLock()) {
            int targetWidth = target.getSize().width;
            if (targetWidth == 0) {
                targetWidth = Integer.MAX_VALUE;
            }

            int hgap = getHgap();
            int vgap = getVgap();
            Insets insets = target.getInsets();
            int maxWidth = targetWidth - (insets.left + insets.right + hgap * 2);
            int x = 0, y = insets.top, rowHeight = 0, maxRowWidth = 0;

            int nmembers = target.getComponentCount();
            for (int i = 0; i < nmembers; i++) {
                Component c = target.getComponent(i);
                if (!c.isVisible()) continue;
                Dimension d = preferred ? c.getPreferredSize() : c.getMinimumSize();
                int w = Math.min(d.width, maxWidth);

                if (x == 0 || x + w <= maxWidth) {
                    if (x > 0) x += hgap;
                    x += w;
                } else {
                    x = w;
                    y += vgap + rowHeight;
                    rowHeight = 0;
                }
                rowHeight = Math.max(rowHeight, d.height);
                maxRowWidth = Math.max(maxRowWidth, x);
            }
            y += rowHeight + insets.bottom + 10;
            return new Dimension(maxRowWidth + insets.left + insets.right, y);
        }
    }

    @Override
    public void layoutContainer(Container target) {
        synchronized (target.getTreeLock()) {
            int hgap = getHgap();
            int vgap = getVgap();
            Insets insets = target.getInsets();
            int maxWidth = target.getWidth() - (insets.left + insets.right + hgap * 2);
            int x = insets.left, y = insets.top, rowHeight = 0;

            int nmembers = target.getComponentCount();
            for (int i = 0; i < nmembers; i++) {
                Component c = target.getComponent(i);
                if (!c.isVisible()) continue;
                Dimension d = c.getPreferredSize();
                int w = Math.min(d.width, maxWidth);

                if (x + w > maxWidth + insets.left) {
                    x = insets.left;
                    y += vgap + rowHeight;
                    rowHeight = 0;
                }
                c.setBounds(x, y, w, d.height);
                x += w + hgap;
                rowHeight = Math.max(rowHeight, d.height);
            }
        }
    }
}
