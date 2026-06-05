package com.it.ui;

import com.it.model.CartItem;
import com.it.model.Order;
import com.it.service.OrderService;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderManagePanel extends JPanel {
    private final OrderService orderService;
    private final JLabel todaySalesLabel;
    private final JPanel orderListPanel;

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public OrderManagePanel(OrderService orderService) {
        this.orderService = orderService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        todaySalesLabel = new JLabel();
        todaySalesLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        topPanel.add(todaySalesLabel);

        JButton refreshBtn = new JButton("새로고침");
        refreshBtn.addActionListener(e -> refresh());
        topPanel.add(refreshBtn);

        add(topPanel, BorderLayout.NORTH);

        orderListPanel = new JPanel();
        orderListPanel.setLayout(new BoxLayout(orderListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(orderListPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("주문 내역"));
        add(scrollPane, BorderLayout.CENTER);

        refresh();
    }

    private static String won(int amount) {
        return String.format("%,d원", amount);
    }

    public void refresh() {
        int totalSales = orderService.getTodayTotalSales();
        todaySalesLabel.setText("오늘 총 매출: " + won(totalSales));

        orderListPanel.removeAll();

        java.util.List<Order> todayOrders = orderService.getTodayOrders();
        if (todayOrders.isEmpty()) {
            JLabel emptyLabel = new JLabel("아직 주문 내역이 없습니다.", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Dialog", Font.ITALIC, 14));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            orderListPanel.add(Box.createVerticalStrut(30));
            orderListPanel.add(emptyLabel);
        } else {
            for (int i = todayOrders.size() - 1; i >= 0; i--) {
                orderListPanel.add(createOrderCard(todayOrders.get(i)));
            }
        }

        orderListPanel.revalidate();
        orderListPanel.repaint();
    }

    private JPanel createOrderCard(Order order) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));

        String timeStr = order.getTimestamp().format(TIME_FMT);
        JLabel timeLabel = new JLabel(timeStr);
        timeLabel.setFont(new Font("Dialog", Font.BOLD, 13));
        card.add(timeLabel, BorderLayout.NORTH);

        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        List<CartItem> items = order.getItems();
        for (CartItem item : items) {
            StringBuilder sb = new StringBuilder(item.getMenuItem().getName());
            if (!item.getOptions().isEmpty()) {
                sb.append(" (").append(item.getOptions()).append(")");
            }
            sb.append("  x").append(item.getQuantity());
            JLabel itemLabel = new JLabel(sb.toString());
            itemLabel.setFont(new Font("Dialog", Font.PLAIN, 13));
            itemsPanel.add(itemLabel);
        }
        card.add(itemsPanel, BorderLayout.CENTER);

        JPanel bottomRow = new JPanel(new BorderLayout());
        JLabel paymentLabel = new JLabel(order.getPaymentMethod());
        paymentLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        bottomRow.add(paymentLabel, BorderLayout.WEST);

        JLabel totalLabel = new JLabel(won(order.getTotalPrice()), SwingConstants.RIGHT);
        totalLabel.setFont(new Font("Dialog", Font.BOLD, 15));
        bottomRow.add(totalLabel, BorderLayout.EAST);
        card.add(bottomRow, BorderLayout.SOUTH);

        return card;
    }
}
