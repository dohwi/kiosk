package com.it.ui;

import com.it.model.CartItem;
import com.it.service.CartService;
import com.it.service.OrderService;
import javax.swing.*;
import java.awt.*;

public class CartPanel extends JPanel {
    private final CartService cartService;
    private final OrderService orderService;
    private final JPanel cartItemPanel;
    private final JLabel totalLabel;

    public CartPanel(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("장바구니"));
        setPreferredSize(new Dimension(340, 0));

        cartItemPanel = new JPanel();
        cartItemPanel.setLayout(new BoxLayout(cartItemPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(cartItemPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        totalLabel = new JLabel("합계: 0원");
        totalLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        bottomPanel.add(totalLabel, BorderLayout.CENTER);

        JButton orderButton = new JButton("주문하기");
        orderButton.setFont(new Font("Dialog", Font.BOLD, 16));
        orderButton.setBackground(new Color(70, 130, 180));
        orderButton.setForeground(Color.WHITE);
        orderButton.addActionListener(e -> onOrder());
        bottomPanel.add(orderButton, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        refresh();
    }

    private static String won(int amount) {
        return String.format("%,d원", amount);
    }

    private void onOrder() {
        java.util.List<CartItem> items = cartService.getItems();
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "장바구니가 비어있습니다.", "알림", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (CartItem item : items) {
            sb.append(item.getMenuItem().getName());
            if (!item.getOptions().isEmpty()) {
                sb.append(" (").append(item.getOptions()).append(")");
            }
            if (item.getUnitPrice() != item.getMenuItem().getPrice()) {
                sb.append(" ").append(won(item.getUnitPrice()));
            }
            sb.append("  x").append(item.getQuantity())
                    .append("  ").append(won(item.getTotalPrice())).append("\n");
        }
        sb.append("\n총 합계: ").append(won(cartService.getTotalPrice()));

        int result = JOptionPane.showConfirmDialog(this,
                sb.toString(),
                "주문 확인",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE);

        if (result != JOptionPane.YES_OPTION) return;

        String[] paymentOptions = {"카드", "현금"};
        int paymentChoice = JOptionPane.showOptionDialog(this,
                "결제 방식을 선택하세요.",
                "결제",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                paymentOptions,
                paymentOptions[0]);

        if (paymentChoice < 0) return;

        String paymentMethod = paymentOptions[paymentChoice];
        orderService.addOrder(
                new java.util.ArrayList<>(items),
                cartService.getTotalPrice(),
                paymentMethod);
        cartService.clear();
        refresh();
        JOptionPane.showMessageDialog(this,
                "주문이 완료되었습니다! (" + paymentMethod + ")",
                "주문 완료",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void refresh() {
        cartItemPanel.removeAll();

        java.util.List<CartItem> items = cartService.getItems();
        for (int i = 0; i < items.size(); i++) {
            cartItemPanel.add(createCartItemRow(items.get(i), i));
        }

        totalLabel.setText("합계: " + won(cartService.getTotalPrice()));

        cartItemPanel.revalidate();
        cartItemPanel.repaint();
    }

    private JPanel createCartItemRow(CartItem item, int index) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 3, 0, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        JLabel nameLabel = new JLabel(item.getMenuItem().getName());
        namePanel.add(nameLabel);
        if (!item.getOptions().isEmpty()) {
            JLabel optLabel = new JLabel(item.getOptions());
            optLabel.setFont(new Font("Dialog", Font.PLAIN, 11));
            optLabel.setForeground(Color.GRAY);
            namePanel.add(optLabel);
        }

        gbc.gridx = 0;
        gbc.weightx = 1.0;
        row.add(namePanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        JButton minusBtn = new JButton("-");
        minusBtn.setPreferredSize(new Dimension(35, 28));
        minusBtn.setMargin(new Insets(0, 0, 0, 0));
        minusBtn.addActionListener(e -> {
            cartService.decreaseQuantity(index);
            refresh();
        });
        row.add(minusBtn, gbc);

        gbc.gridx = 2;
        JLabel qtyLabel = new JLabel(String.valueOf(item.getQuantity()), SwingConstants.CENTER);
        qtyLabel.setPreferredSize(new Dimension(30, 20));
        row.add(qtyLabel, gbc);

        gbc.gridx = 3;
        JButton plusBtn = new JButton("+");
        plusBtn.setPreferredSize(new Dimension(35, 28));
        plusBtn.setMargin(new Insets(0, 0, 0, 0));
        plusBtn.addActionListener(e -> {
            cartService.updateQuantity(index, item.getQuantity() + 1);
            refresh();
        });
        row.add(plusBtn, gbc);

        gbc.gridx = 4;
        gbc.ipadx = 10;
        JLabel priceLabel = new JLabel(won(item.getTotalPrice()), SwingConstants.RIGHT);
        priceLabel.setPreferredSize(new Dimension(75, 20));
        row.add(priceLabel, gbc);

        gbc.gridx = 5;
        gbc.ipadx = 0;
        JButton removeBtn = new JButton("삭제");
        removeBtn.setPreferredSize(new Dimension(50, 28));
        removeBtn.setMargin(new Insets(0, 0, 0, 0));
        removeBtn.addActionListener(e -> {
            cartService.removeItem(index);
            refresh();
        });
        row.add(removeBtn, gbc);

        return row;
    }
}
