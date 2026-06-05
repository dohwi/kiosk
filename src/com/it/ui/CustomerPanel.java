package com.it.ui;

import com.it.data.MenuData;
import com.it.service.CartService;
import com.it.service.OrderService;
import javax.swing.*;

public class CustomerPanel extends JPanel {
    private final MenuPanel menuPanel;
    private final CartPanel cartPanel;
    private final JFrame ownerFrame;

    public CustomerPanel(MenuData menuData, CartService cartService, OrderService orderService, JFrame ownerFrame) {
        this.ownerFrame = ownerFrame;
        menuPanel = new MenuPanel(menuData);
        cartPanel = new CartPanel(cartService, orderService);

        menuPanel.setOnItemSelected(item -> {
            OrderDetailDialog dialog = new OrderDetailDialog(ownerFrame, item, (menuItem, options, unitPrice) -> {
                cartService.addItem(menuItem, options, unitPrice);
                cartPanel.refresh();
            });
            dialog.setVisible(true);
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPanel, cartPanel);
        splitPane.setDividerLocation(580);
        splitPane.setResizeWeight(0.68);

        setLayout(new java.awt.BorderLayout());
        add(splitPane, java.awt.BorderLayout.CENTER);
    }

    public void refresh() {
        menuPanel.refresh();
        cartPanel.refresh();
    }
}
