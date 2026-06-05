package com.it.ui;

import com.it.data.MenuData;
import com.it.model.Category;
import com.it.model.MenuItem;
import com.it.service.AdminService;
import com.it.service.OrderService;
import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {
    private final AdminService adminService;
    private DefaultListModel<String> listModel;
    private JList<String> menuList;
    private JTextField nameField;
    private JTextField priceField;
    private Category selectedCategory = Category.COFFEE;
    private final java.util.Map<Category, JRadioButton> categoryRadios = new java.util.EnumMap<>(Category.class);
    private final OrderManagePanel orderManagePanel;

    public AdminPanel(MenuData menuData, OrderService orderService) {
        this.adminService = new AdminService(menuData);

        orderManagePanel = new OrderManagePanel(orderService);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("메뉴 관리", createMenuManagePanel());
        tabbedPane.addTab("주문 관리", orderManagePanel);

        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createMenuManagePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        listModel = new DefaultListModel<>();
        menuList = new JList<>(listModel);
        menuList.setFont(new Font("Dialog", Font.PLAIN, 14));
        JScrollPane listScroll = new JScrollPane(menuList);
        listScroll.setBorder(BorderFactory.createTitledBorder("메뉴 목록"));
        listScroll.setPreferredSize(new Dimension(350, 0));
        panel.add(listScroll, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("메뉴 추가"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("이름:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("가격:"), gbc);
        gbc.gridx = 1;
        priceField = new JTextField(15);
        formPanel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("카테고리:"), gbc);

        gbc.gridx = 1;
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        ButtonGroup group = new ButtonGroup();
        boolean first = true;
        for (Category cat : Category.values()) {
            JRadioButton radio = new JRadioButton(cat.getDisplayName(), first);
            group.add(radio);
            categoryRadios.put(cat, radio);
            radio.addActionListener(e -> {
                if (radio.isSelected()) selectedCategory = cat;
            });
            radioPanel.add(radio);
            first = false;
        }
        formPanel.add(radioPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton addButton = new JButton("추가");
        addButton.addActionListener(e -> addItem());
        formPanel.add(addButton, gbc);

        rightPanel.add(formPanel, BorderLayout.NORTH);

        JButton deleteButton = new JButton("선택한 메뉴 삭제");
        deleteButton.addActionListener(e -> deleteItem());
        rightPanel.add(deleteButton, BorderLayout.SOUTH);

        panel.add(rightPanel, BorderLayout.EAST);

        refreshList();
        return panel;
    }

    private void addItem() {
        String name = nameField.getText().trim();
        String priceStr = priceField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "이름을 입력하세요.", "오류", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceStr);
            if (price <= 0) {
                JOptionPane.showMessageDialog(this, "가격은 0보다 커야 합니다.", "오류", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "가격은 숫자로 입력하세요.", "오류", JOptionPane.WARNING_MESSAGE);
            return;
        }

        adminService.addItem(name, price, selectedCategory);
        nameField.setText("");
        priceField.setText("");
        refreshList();
    }

    private void deleteItem() {
        int selected = menuList.getSelectedIndex();
        if (selected < 0) {
            JOptionPane.showMessageDialog(this, "삭제할 항목을 선택하세요.", "알림", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "정말 삭제하시겠습니까?",
                "삭제 확인",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            adminService.removeItem(selected);
            refreshList();
        }
    }

    public void refreshList() {
        listModel.clear();
        for (MenuItem item : adminService.getItems()) {
            listModel.addElement(item.getCategory().getDisplayName() + " | " + item.getName() + " | " + String.format("%,d원", item.getPrice()));
        }
    }

    public void refresh() {
        refreshList();
        orderManagePanel.refresh();
    }
}
