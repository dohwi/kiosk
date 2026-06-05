package com.it.ui;

import com.it.model.MenuItem;
import com.it.model.Size;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.ObjIntConsumer;

@FunctionalInterface
interface AddToCartCallback {
    void accept(MenuItem item, String options, int unitPrice);
}

public class OrderDetailDialog extends JDialog {
    private final MenuItem menuItem;
    private final AddToCartCallback onAddToCart;
    private int quantity = 1;
    private int unitPrice;
    private JLabel qtyLabel;
    private JLabel totalPriceLabel;
    private JRadioButton hotRadio;
    private JRadioButton iceRadio;
    private JRadioButton sizeS;
    private JRadioButton sizeM;
    private JRadioButton sizeL;

    public OrderDetailDialog(JFrame owner, MenuItem menuItem, AddToCartCallback onAddToCart) {
        super(owner, menuItem.getName(), true);
        this.menuItem = menuItem;
        this.onAddToCart = onAddToCart;
        this.unitPrice = menuItem.getPrice();
        buildUI();
    }

    private void buildUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        JLabel imageLabel = new JLabel(createPlaceholderImage(menuItem.getName(), menuItem.getCategory().getColor(), 200, 150));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(imageLabel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JLabel nameLabel = new JLabel(menuItem.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        infoPanel.add(nameLabel);

        JLabel catLabel = new JLabel(menuItem.getCategory().getDisplayName(), SwingConstants.CENTER);
        catLabel.setFont(new Font("Dialog", Font.ITALIC, 13));
        catLabel.setForeground(Color.GRAY);
        infoPanel.add(catLabel);

        topPanel.add(infoPanel, BorderLayout.SOUTH);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JPanel sizePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        sizePanel.setBorder(BorderFactory.createTitledBorder("사이즈"));
        ButtonGroup sizeGroup = new ButtonGroup();
        sizeS = new JRadioButton("S", true);
        sizeM = new JRadioButton("M (+500원)");
        sizeL = new JRadioButton("L (+1,000원)");
        sizeGroup.add(sizeS);
        sizeGroup.add(sizeM);
        sizeGroup.add(sizeL);
        sizeS.addActionListener(e -> updatePrice(Size.S));
        sizeM.addActionListener(e -> updatePrice(Size.M));
        sizeL.addActionListener(e -> updatePrice(Size.L));
        sizePanel.add(sizeS);
        sizePanel.add(sizeM);
        sizePanel.add(sizeL);
        centerPanel.add(sizePanel);

        boolean isBeverage = menuItem.getCategory() == com.it.model.Category.COFFEE
                || menuItem.getCategory() == com.it.model.Category.DRINK;

        if (isBeverage) {
            centerPanel.add(Box.createVerticalStrut(5));
            JPanel tempPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            tempPanel.setBorder(BorderFactory.createTitledBorder("온도 선택"));
            ButtonGroup tempGroup = new ButtonGroup();
            hotRadio = new JRadioButton("HOT", true);
            iceRadio = new JRadioButton("ICE");
            tempGroup.add(hotRadio);
            tempGroup.add(iceRadio);
            tempPanel.add(hotRadio);
            tempPanel.add(iceRadio);
            centerPanel.add(tempPanel);
        }

        centerPanel.add(Box.createVerticalStrut(5));

        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        quantityPanel.setBorder(BorderFactory.createTitledBorder("수량"));
        JButton minusBtn = new JButton("-");
        minusBtn.setPreferredSize(new Dimension(40, 32));
        minusBtn.setMargin(new Insets(0, 0, 0, 0));
        minusBtn.addActionListener(e -> updateQuantity(-1));
        quantityPanel.add(minusBtn);

        qtyLabel = new JLabel("1", SwingConstants.CENTER);
        qtyLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        qtyLabel.setPreferredSize(new Dimension(40, 32));
        quantityPanel.add(qtyLabel);

        JButton plusBtn = new JButton("+");
        plusBtn.setPreferredSize(new Dimension(40, 32));
        plusBtn.setMargin(new Insets(0, 0, 0, 0));
        plusBtn.addActionListener(e -> updateQuantity(1));
        quantityPanel.add(plusBtn);
        centerPanel.add(quantityPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));

        totalPriceLabel = new JLabel("합계: " + String.format("%,d원", unitPrice * quantity), SwingConstants.CENTER);
        totalPriceLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        bottomPanel.add(totalPriceLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton addButton = new JButton("장바구니에 담기");
        addButton.setPreferredSize(new Dimension(150, 40));
        addButton.setFont(new Font("Dialog", Font.BOLD, 14));
        addButton.addActionListener(e -> onAdd());

        JButton cancelButton = new JButton("취소");
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void updatePrice(Size size) {
        unitPrice = menuItem.getPrice() + size.getExtraPrice();
        updateTotalLabel();
    }

    private void updateQuantity(int delta) {
        quantity = Math.max(1, Math.min(99, quantity + delta));
        qtyLabel.setText(String.valueOf(quantity));
        updateTotalLabel();
    }

    private void updateTotalLabel() {
        totalPriceLabel.setText("합계: " + String.format("%,d원", unitPrice * quantity));
    }

    private void onAdd() {
        StringBuilder sb = new StringBuilder();
        if (sizeM.isSelected()) {
            sb.append("M");
        } else if (sizeL.isSelected()) {
            sb.append("L");
        } else {
            sb.append("S");
        }

        if (hotRadio != null && iceRadio != null) {
            sb.append(" / ");
            sb.append(hotRadio.isSelected() ? "HOT" : "ICE");
        }

        for (int i = 0; i < quantity; i++) {
            onAddToCart.accept(menuItem, sb.toString(), unitPrice);
        }
        dispose();
    }

    static ImageIcon createPlaceholderImage(String name, Color bgColor, int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(bgColor);
        g2d.fillRoundRect(0, 0, w, h, 20, 20);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Dialog", Font.BOLD, 18));
        FontMetrics fm = g2d.getFontMetrics();
        String text = name;
        if (fm.stringWidth(text) > w - 20) {
            text = name.substring(0, 4);
        }
        int tw = fm.stringWidth(text);
        int th = fm.getAscent();
        g2d.drawString(text, (w - tw) / 2, h / 2 + th / 3);
        g2d.dispose();
        return new ImageIcon(img);
    }
}
