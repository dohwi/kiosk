package com.it.ui;

import com.it.data.MenuData;
import com.it.service.CartService;
import com.it.service.OrderService;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final String ADMIN_PASSWORD = "1234";

    private final MenuData menuData;
    private final CartService cartService;
    private final OrderService orderService;
    private final CustomerPanel customerPanel;
    private final AdminPanel adminPanel;
    private final CardLayout cardLayout;
    private final JPanel contentPanel;
    private final JButton customerBtn;
    private final JButton adminBtn;

    public MainFrame() {
        menuData = new MenuData();
        cartService = new CartService();
        orderService = new OrderService();

        setTitle("키오스크");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        customerBtn = new JButton("고객 모드");
        adminBtn = new JButton("관리자 모드");
        customerBtn.setPreferredSize(new Dimension(120, 40));
        adminBtn.setPreferredSize(new Dimension(120, 40));
        topPanel.add(customerBtn);
        topPanel.add(adminBtn);
        add(topPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        customerPanel = new CustomerPanel(menuData, cartService, orderService, this);
        adminPanel = new AdminPanel(menuData, orderService);

        contentPanel.add(customerPanel, "customer");
        contentPanel.add(adminPanel, "admin");
        add(contentPanel, BorderLayout.CENTER);

        customerBtn.addActionListener(e -> switchToCustomer());
        adminBtn.addActionListener(e -> switchToAdmin());

        highlightButton(customerBtn);
        cardLayout.show(contentPanel, "customer");
    }

    private void switchToCustomer() {
        cardLayout.show(contentPanel, "customer");
        customerPanel.refresh();
        highlightButton(customerBtn);
    }

    private void switchToAdmin() {
        JPasswordField pf = new JPasswordField();
        pf.setPreferredSize(new Dimension(120, 28));
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.add(new JLabel("비밀번호:"));
        panel.add(pf);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "관리자 인증", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        if (!ADMIN_PASSWORD.equals(new String(pf.getPassword()))) {
            JOptionPane.showMessageDialog(this,
                    "비밀번호가 틀렸습니다.", "인증 실패",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        cardLayout.show(contentPanel, "admin");
        adminPanel.refresh();
        highlightButton(adminBtn);
    }

    private void highlightButton(JButton active) {
        Color defaultBg = UIManager.getColor("Button.background");
        customerBtn.setBackground(customerBtn == active ? new Color(70, 130, 180) : defaultBg);
        customerBtn.setForeground(customerBtn == active ? Color.WHITE : Color.BLACK);
        adminBtn.setBackground(adminBtn == active ? new Color(70, 130, 180) : defaultBg);
        adminBtn.setForeground(adminBtn == active ? Color.WHITE : Color.BLACK);
    }
}
