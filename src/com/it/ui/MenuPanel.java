package com.it.ui;

import com.it.model.Category;
import com.it.model.MenuItem;
import com.it.data.MenuData;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MenuPanel extends JPanel {
    private static final int CARD_WIDTH = 170;
    private static final int CARD_HEIGHT = 160;
    private static final int IMG_WIDTH = 130;
    private static final int IMG_HEIGHT = 85;

    private final MenuData menuData;
    private final JPanel itemGrid;
    private final Map<Category, JRadioButton> categoryRadios = new EnumMap<>(Category.class);
    private final JRadioButton allRadio;
    private Consumer<MenuItem> onItemSelected;
    private Category selectedCategory;

    public MenuPanel(MenuData menuData) {
        this.menuData = menuData;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("메뉴"));

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        ButtonGroup group = new ButtonGroup();

        allRadio = new JRadioButton("전체", true);
        group.add(allRadio);
        allRadio.addActionListener(e -> onCategoryChanged(null));
        radioPanel.add(allRadio);

        for (Category cat : Category.values()) {
            JRadioButton radio = new JRadioButton(cat.getDisplayName());
            radio.addActionListener(e -> onCategoryChanged(cat));
            group.add(radio);
            categoryRadios.put(cat, radio);
            radioPanel.add(radio);
        }

        add(radioPanel, BorderLayout.NORTH);

        itemGrid = new JPanel();
        itemGrid.setLayout(new WrapLayout(FlowLayout.LEFT, 12, 12));
        JScrollPane scrollPane = new JScrollPane(itemGrid);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        refreshItems(null);
    }

    public void setOnItemSelected(Consumer<MenuItem> callback) {
        this.onItemSelected = callback;
    }

    private void onCategoryChanged(Category category) {
        selectedCategory = category;
        refreshItems(category);
    }

    public void refreshItems(Category category) {
        itemGrid.removeAll();

        List<MenuItem> items;
        if (category == null) {
            items = menuData.getItems();
        } else {
            items = menuData.getItemsByCategory(category);
        }

        for (MenuItem item : items) {
            itemGrid.add(createItemCard(item));
        }

        itemGrid.revalidate();
        itemGrid.repaint();
    }

    private JPanel createItemCard(MenuItem item) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        card.setMaximumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        card.setMinimumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        card.setBackground(Color.WHITE);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        ImageIcon icon = createPlaceholderImage(item.getName(), item.getCategory().getColor(), IMG_WIDTH, IMG_HEIGHT);
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(imageLabel);

        card.add(Box.createVerticalStrut(6));

        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setMaximumSize(new Dimension(CARD_WIDTH - 16, 22));
        card.add(nameLabel);

        JLabel priceLabel = new JLabel(String.format("%,d원", item.getPrice()));
        priceLabel.setFont(new Font("Dialog", Font.PLAIN, 13));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(priceLabel);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                        BorderFactory.createEmptyBorder(7, 7, 7, 7)));
                card.setBackground(new Color(245, 248, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)));
                card.setBackground(Color.WHITE);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (onItemSelected != null) {
                    onItemSelected.accept(item);
                }
            }
        });

        return card;
    }

    static ImageIcon createPlaceholderImage(String name, Color bgColor, int w, int h) {
        return OrderDetailDialog.createPlaceholderImage(name, bgColor, w, h);
    }

    public void refresh() {
        refreshItems(selectedCategory);
    }
}
