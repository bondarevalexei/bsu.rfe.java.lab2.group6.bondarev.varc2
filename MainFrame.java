import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("serial")
// Главный класс приложения, он же класс фрейма
public class MainFrame extends JFrame {
    // Размеры окна приложения в виде констант
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;

    // Текстовые поля для считывания значений переменных,
    // как компоненты, совместно используемые в различных методах
    private JTextField textFieldX;
    private JTextField textFieldY;
    private JTextField textFieldZ;


    private JTextField textFieldMem1;
    private JTextField textFieldMem2;
    private JTextField textFieldMem3;

    // Текстовое поле для отображения результата,
    // как компонент, совместно используемый в различных методах
    private JTextField textFieldResult;
    // Группа радио-кнопок для обеспечения уникальности выделения в группе
    private ButtonGroup radioButtons = new ButtonGroup();
    private ButtonGroup variableRadioButtons = new ButtonGroup();
    // Контейнер для отображения радио-кнопок
    private Box hboxFormulaType = Box.createHorizontalBox();
    private Box hboxVariable = Box.createHorizontalBox();

    private JPanel _panel1 = new JPanel();
    private JPanel _panel2 = new JPanel();
    private JPanel imagePanel = _panel1;
    private JPanel memPanel = new JPanel();
    private int formulaId = 1;
    private int variableId = 1;

    private double mem1 = 0;
    private double mem2 = 0;
    private double mem3 = 0;

    public Double calculate1(Double x, Double y, Double z) {
        return Math.pow((Math.log((1+x)*(1+x))+Math.cos(Math.PI*z*z*z)),Math.sin(y))+
                Math.pow((Math.exp(x*x) + Math.cos(Math.exp(z))+Math.sqrt(1/y)),1/x);
    }

    public Double calculate2(Double x, Double y, Double z) {
        return Math.pow((Math.cos(Math.PI*x*x*x)+Math.log((1+y)*(1+y))),1/4)*
                (Math.exp(z*z)+Math.sqrt(1/x)+Math.cos(Math.exp(y)));
    }

    // Вспомогательный метод для добавления кнопок на панель
    private void addRadioButton(String buttonName, final int formulaId) {
        JRadioButton button = new JRadioButton(buttonName);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                MainFrame.this.formulaId = formulaId;

                updateImagePanel();
            }
        });
        radioButtons.add(button);
        hboxFormulaType.add(button);
    }

    private void addRadButtonForVariable(String buttonName, final int variableId){
        JRadioButton button = new JRadioButton(buttonName);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.this.variableId = variableId;
            }
        });
        variableRadioButtons.add(button);
        hboxVariable.add(button);
    }
    private void updateImagePanel() {
        MainFrame.this.getContentPane().remove(imagePanel);
        if(MainFrame.this.formulaId == 1){
            MainFrame.this.getContentPane().add(_panel1, BorderLayout.NORTH);
            MainFrame.this.imagePanel = MainFrame.this._panel1;
        }
        else {
            MainFrame.this.getContentPane().add(_panel2, BorderLayout.NORTH);
            MainFrame.this.imagePanel = MainFrame.this._panel2;
        }

        imagePanel.revalidate();
        MainFrame.this.repaint();
    }

    private void updateMemPanel(){
        MainFrame.this.textFieldMem1.setText(Double.toString(mem1));
        MainFrame.this.textFieldMem2.setText(Double.toString(mem2));
        MainFrame.this.textFieldMem3.setText(Double.toString(mem3));

        MainFrame.this.getContentPane().repaint();
    }

    // Конструктор класса
    public MainFrame() {
        super("Вычисление формулы");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        // Отцентрировать окно приложения на экране
        setLocation((kit.getScreenSize().width - WIDTH) / 2,
                (kit.getScreenSize().height - HEIGHT) / 2);
        hboxFormulaType.add(Box.createHorizontalGlue());
        addRadioButton("Формула 1", 1);
        addRadioButton("Формула 2", 2);
        radioButtons.setSelected(
                radioButtons.getElements().nextElement().getModel(), true);
        hboxFormulaType.add(Box.createHorizontalGlue());
        hboxFormulaType.setBorder(
                BorderFactory.createLineBorder(Color.YELLOW));

        // Создать область с полями ввода для X и Y и Z
        JLabel labelForX = new JLabel("X:");
        textFieldX = new JTextField("0", 10);
        textFieldX.setMaximumSize(textFieldX.getPreferredSize());
        JLabel labelForY = new JLabel("Y:");
        textFieldY = new JTextField("0", 10);
        textFieldY.setMaximumSize(textFieldY.getPreferredSize());
        JLabel labelForZ = new JLabel("Z:");
        textFieldZ = new JTextField("0",10);
        textFieldZ.setMaximumSize(textFieldZ.getPreferredSize());

        Box hboxVariables = Box.createHorizontalBox();
        hboxVariables.setBorder(BorderFactory.createLineBorder(Color.RED));
        hboxVariables.add(Box.createHorizontalGlue());
        hboxVariables.add(labelForX);
        hboxVariables.add(Box.createHorizontalStrut(10));
        hboxVariables.add(textFieldX);
        hboxVariables.add(Box.createHorizontalStrut(100));
        hboxVariables.add(labelForY);
        hboxVariables.add(Box.createHorizontalStrut(10));
        hboxVariables.add(textFieldY);
        hboxVariables.add(Box.createHorizontalStrut(100));
        hboxVariables.add(labelForZ);
        hboxVariables.add(Box.createHorizontalStrut(10));
        hboxVariables.add(textFieldZ);
        hboxVariables.add(Box.createHorizontalGlue());

        // Создать область для вывода результата
        JLabel labelForResult = new JLabel("Результат:");
        textFieldResult = new JTextField("0", 10);
        textFieldResult.setMaximumSize(
                textFieldResult.getPreferredSize());
        Box hboxResult = Box.createHorizontalBox();
        hboxResult.add(Box.createHorizontalGlue());
        hboxResult.add(labelForResult);
        hboxResult.add(Box.createHorizontalStrut(10));
        hboxResult.add(textFieldResult);
        hboxResult.add(Box.createHorizontalGlue());
        hboxResult.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        // Создать область для кнопок
        JButton buttonCalc = new JButton("Вычислить");
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    Double x = Double.parseDouble(textFieldX.getText());
                    mem1 = x;
                    Double y = Double.parseDouble(textFieldY.getText());
                    mem2 = y;
                    Double z = Double.parseDouble(textFieldZ.getText());
                    mem3 = z;
                    Double result;
                    if (formulaId == 1)
                        result = calculate1(x, y, z);
                    else
                        result = calculate2(x, y, z);
                    textFieldResult.setText(result.toString());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа",
                            JOptionPane.WARNING_MESSAGE);
                }
                updateMemPanel();
            }
        });
        JButton buttonReset = new JButton("Очистить поля");
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                textFieldX.setText("0");
                textFieldY.setText("0");
                textFieldZ.setText("0");
                textFieldResult.setText("0");
                mem1 = mem2 = mem3 = 0;
                updateMemPanel();
            }
        });
        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.setBorder(
                BorderFactory.createLineBorder(Color.GREEN));

        try {
            _panel1.add(new JLabel(new ImageIcon(ImageIO.read(new File("img//f1.bmp")))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            _panel2.add(new JLabel(new ImageIcon(ImageIO.read(new File("img//f2.bmp")))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        hboxVariable.add(Box.createHorizontalGlue());
        addRadButtonForVariable("Переменная 1", 1);
        addRadButtonForVariable("Переменная 2", 2);
        addRadButtonForVariable("Переменная 3", 3);
        variableRadioButtons.setSelected(radioButtons.getElements().nextElement().getModel(), true);
        hboxVariable.add(Box.createHorizontalGlue());
        hboxVariable.setBorder(BorderFactory.createLineBorder(Color.CYAN));

        JButton buttonMC = new JButton("MC");
        buttonMC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (variableId){
                    case 1:
                        textFieldX.setText("0");
                        mem1=0;
                        break;
                    case 2:
                        textFieldY.setText("0");
                        mem2=0;
                        break;
                    case 3:
                        textFieldZ.setText("0");
                        mem3=0;
                        break;
                }
                updateMemPanel();
            }
        });
        JButton buttonMPlus = new JButton("M+");
        buttonMPlus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (variableId) {
                    case 1 -> {
                        textFieldResult.setText(Double.toString(Double.parseDouble(textFieldResult.getText()) + Double.parseDouble(textFieldX.getText())));
                        mem1 = Double.parseDouble(textFieldResult.getText());
                    }
                    case 2 -> {
                        textFieldResult.setText(Double.toString(Double.parseDouble(textFieldResult.getText()) + Double.parseDouble(textFieldY.getText())));
                        mem2 = Double.parseDouble(textFieldResult.getText());
                    }
                    case 3 -> {
                        textFieldResult.setText(Double.toString(Double.parseDouble(textFieldResult.getText()) + Double.parseDouble(textFieldZ.getText())));
                        mem3 = Double.parseDouble(textFieldResult.getText());
                    }
                }
                updateMemPanel();
            }
        });
        Box hboxButtonsM = Box.createHorizontalBox();
        hboxButtonsM.add(Box.createHorizontalGlue());
        hboxButtonsM.add(buttonMC);
        hboxButtonsM.add(Box.createHorizontalStrut(30));
        hboxButtonsM.add(buttonMPlus);
        hboxButtonsM.add(Box.createHorizontalGlue());
        hboxButtonsM.setBorder(BorderFactory.createLineBorder(Color.magenta));

        JLabel labelForMem1 = new JLabel("mem1");
        textFieldMem1 = new JTextField(Double.toString(mem1), 10);
        textFieldMem1.setMaximumSize(textFieldMem1.getPreferredSize());
        JLabel labelForMem2 = new JLabel("mem2");
        textFieldMem2 = new JTextField(Double.toString(mem2), 10);
        textFieldMem2.setMaximumSize(textFieldMem2.getPreferredSize());
        JLabel labelForMem3 = new JLabel("mem1");
        textFieldMem3 = new JTextField(Double.toString(mem2), 10);
        textFieldMem3.setMaximumSize(textFieldMem2.getPreferredSize());

        memPanel.add(textFieldMem1);
        memPanel.add(textFieldMem2);
        memPanel.add(textFieldMem3);

        // Связать области воедино в компоновке BoxLayout
        Box contentBox = Box.createVerticalBox();
        contentBox.add(Box.createVerticalGlue());
        contentBox.add(hboxFormulaType);
        contentBox.add(hboxVariables);
        contentBox.add(hboxResult);
        contentBox.add(hboxButtons);
        contentBox.add(hboxVariable);
        contentBox.add(hboxButtonsM);
        contentBox.add(Box.createVerticalGlue());
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(contentBox, BorderLayout.CENTER);
        getContentPane().add(_panel1, BorderLayout.NORTH);
        getContentPane().add(memPanel, BorderLayout.SOUTH);
    }

    // Главный метод класса
    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}