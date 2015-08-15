/**
 *
 */
package net.magriso.srt.editor;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.NumberFormatter;

import net.magriso.JFrameExt;
import net.magriso.srt.editor.common.Closable;
import net.magriso.srt.editor.error.MultiValidationException;
import net.magriso.srt.editor.listener.DefaultWindowListener;
import net.magriso.srt.editor.listener.SingleSelectSelectionListener;
import net.magriso.srt.editor.text.PaginationTextField;
import net.magriso.srt.editor.text.TextAreaEditor;
import net.magriso.srt.editor.text.TextAreaRenderer;

/**
 * The Class SRTEditorMain.
 *
 * @author Yakir
 */
public class SRTEditorMain extends JFrameExt implements Closable
{

	/** The txt filename. */
	private JTextField txtFilename;

	/** The btn save. */
	private JButton btnSave;

	/** The chk backup. */
	private JCheckBox chkBackup;

	/** The txt selected. */
	private JTextField txtSelected;

	/** The txt increase by. */
	private JFormattedTextField txtIncreaseBy;

	/** The btn increase. */
	private JButton btnIncrease;

	/** The hyp validation. */
	private JButton hypValidation;

	/** The btn first. */
	private JButton btnFirst;

	/** The btn prev. */
	private JButton btnPrev;

	/** The txt current page. */
	private PaginationTextField txtCurrentPage;

	/** The btn next. */
	private JButton btnNext;

	/** The btn last. */
	private JButton btnLast;

	/** The srt object table. */
	JTable srtObjectTable;

	/** The scroll pane. */
	private JScrollPane scrollPane;

	/** The srt object. */
	private SRTObject srtObject;

	/** The single select selection listener. */
	private SingleSelectSelectionListener singleSelectSelectionListener;

	/** The column names. */
	private String[] columnNames;

	/**
	 * Instantiates a new SRT editor main.
	 *
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public SRTEditorMain() throws FileNotFoundException, IOException
	{
		super();
		initMenuBar();

		pack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.magriso.JFrameExt#initComponents()
	 */
	@Override
	protected void initComponents()
	{
		JLabel lblFilename = new JLabel();
		lblFilename.setText(getString("lblFilename"));

		txtFilename = new JTextField();
		txtFilename.setEditable(false);
		Dimension maxSize = txtFilename.getMaximumSize();
		maxSize.width = 550;
		txtFilename.setMaximumSize(maxSize);

		JButton btnLoadSubtitlesFile = new JButton();
		try
		{
			btnLoadSubtitlesFile.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
					"toolbarButtonGraphics/general/Open16.gif")));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			btnLoadSubtitlesFile.setText(getString("btnLoadSubtitlesFile"));
		}

		btnLoadSubtitlesFile.setToolTipText(getString("btnLoadSubtitlesFileToolTip"));
		btnLoadSubtitlesFile.addActionListener(new ActionListener()
		{

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			@Override
			public void actionPerformed(ActionEvent event)
			{
				loadSubtitlesFile();
			}
		});

		btnSave = new JButton();
		try
		{
			btnSave.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
					"toolbarButtonGraphics/general/Save16.gif")));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			btnSave.setText(getString("btnSave"));
		}
		btnSave.setEnabled(false);
		btnSave.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				saveUpdatedSrtFile();
			}
		});

		chkBackup = new JCheckBox();
		// chkBackup.setSelected(true);
		chkBackup.setText(getString("chkBackup"));
		chkBackup.setEnabled(false);

		JLabel lblSelected = new JLabel();
		lblSelected.setText(getString("lblSelected"));

		// Align "Filename:" label with "Start from:" label
		lblFilename.setMaximumSize(new Dimension(lblSelected.getMaximumSize().width,
				lblFilename.getMaximumSize().height));

		txtSelected = new JTextField();
		txtSelected.setEditable(false);
		txtSelected.setHorizontalAlignment(SwingConstants.CENTER);
		maxSize = txtSelected.getMaximumSize();
		maxSize.width = 40;
		txtSelected.setMaximumSize(maxSize);

		JLabel lblIncreaseBy = new JLabel();
		lblIncreaseBy.setText(getString("lblIncreaseBy"));

		txtIncreaseBy = new JFormattedTextField(new NumberFormatter());
		maxSize = txtIncreaseBy.getMaximumSize();
		maxSize.width = 100;
		txtIncreaseBy.setMaximumSize(maxSize);
		txtIncreaseBy.setEditable(false);
		txtIncreaseBy.getDocument().addDocumentListener(new DocumentListener()
		{

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * javax.swing.event.DocumentListener#removeUpdate(javax.swing.event
			 * .DocumentEvent)
			 */
			@Override
			public void removeUpdate(DocumentEvent event)
			{
				txtIncreaseBy_onDocumentChange(event.getDocument());
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * javax.swing.event.DocumentListener#insertUpdate(javax.swing.event
			 * .DocumentEvent)
			 */
			@Override
			public void insertUpdate(DocumentEvent event)
			{
				txtIncreaseBy_onDocumentChange(event.getDocument());
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * javax.swing.event.DocumentListener#changedUpdate(javax.swing.
			 * event.DocumentEvent)
			 */
			@Override
			public void changedUpdate(DocumentEvent event)
			{
				txtIncreaseBy_onDocumentChange(event.getDocument());
			}
		});

		JLabel lblMs = new JLabel();
		lblMs.setText(getString("lblMs"));

		btnIncrease = new JButton();
		btnIncrease.setText(getString("btnIncrease"));
		btnIncrease.setEnabled(false);
		btnIncrease.addActionListener(new ActionListener()
		{

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			@Override
			public void actionPerformed(ActionEvent event)
			{
				handleTimeIncrease();
			}
		});

		hypValidation = new JButton();
		hypValidation.setText(getString("hypValidationDefault"));
		hypValidation.setBorderPainted(false);
		hypValidation.setContentAreaFilled(false);
		hypValidation.setEnabled(false);
		hypValidation.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					srtObject.validate();
				}
				catch(MultiValidationException ex)
				{
					StringBuilder validationErrorMsg = new StringBuilder();
					for(int indx = 0; indx < ex.getExceptions().size(); indx++)
					{
						validationErrorMsg.append(ex.getExceptions().get(indx).getMessage());
						validationErrorMsg.append("\n");
					}
					((JButton) e.getSource()).setToolTipText(validationErrorMsg.toString());
				}
			}
		});

		btnFirst = new JButton();
		try
		{
			btnFirst.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
					"toolbarButtonGraphics/media/Rewind16.gif")));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			btnFirst.setText(getString("btnFirst"));
		}
		btnFirst.setEnabled(false);
		btnFirst.setToolTipText(getString("btnFirstToolTip"));
		btnFirst.addActionListener(new ActionListener()
		{

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			@Override
			public void actionPerformed(ActionEvent event)
			{
				goToFirstPage();
			}
		});

		btnPrev = new JButton();
		try
		{
			btnPrev.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
					"toolbarButtonGraphics/media/StepBack16.gif")));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			btnPrev.setText(getString("btnPrev"));
		}
		btnPrev.setEnabled(false);
		btnPrev.setToolTipText(getString("btnPrevToolTip"));
		btnPrev.addActionListener(new ActionListener()
		{

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			@Override
			public void actionPerformed(ActionEvent event)
			{
				goToPreviousPage();
			}
		});

		txtCurrentPage = new PaginationTextField();
		maxSize = txtCurrentPage.getMaximumSize();
		maxSize.width = 50;
		txtCurrentPage.setMaximumSize(maxSize);
		txtCurrentPage.setHorizontalAlignment(SwingConstants.CENTER);

		btnNext = new JButton();
		try
		{
			btnNext.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
					"toolbarButtonGraphics/media/StepForward16.gif")));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			btnNext.setText(getString("btnNext"));
		}
		btnNext.setEnabled(false);
		btnNext.setToolTipText(getString("btnNextToolTip"));
		btnNext.addActionListener(new ActionListener()
		{

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			@Override
			public void actionPerformed(ActionEvent event)
			{
				goToNextPage();
			}
		});

		btnLast = new JButton();
		try
		{
			btnLast.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
					"toolbarButtonGraphics/media/FastForward16.gif")));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			btnLast.setText(getString("btnLast"));
		}
		btnLast.setEnabled(false);
		btnLast.setToolTipText(getString("btnLastToolTip"));
		btnLast.addActionListener(new ActionListener()
		{

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			@Override
			public void actionPerformed(ActionEvent event)
			{
				goToLastPage();
			}
		});

		Object[][] dataValues = {};
		columnNames = new String[] { getString("col_index"), getString("col_startTime"), getString("col_endTime"),
				getString("col_subtitleText") };
		srtObjectTable = new JTable(dataValues, columnNames)
		{

			/*
			 * (non-Javadoc)
			 * 
			 * @see javax.swing.JTable#isCellEditable(int, int)
			 */
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}

		};
		handleTableDefaults();

		scrollPane = new JScrollPane(srtObjectTable);
		scrollPane.setVerticalScrollBar(new JScrollBar());
		scrollPane.getVerticalScrollBar().setUnitIncrement(32);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup().addComponent(lblFilename).addComponent(txtFilename)
								.addComponent(btnLoadSubtitlesFile).addComponent(btnSave).addComponent(chkBackup))
				.addGroup(
						Alignment.LEADING,
						layout.createSequentialGroup().addComponent(lblSelected).addComponent(txtSelected).addGap(50)
								.addComponent(lblIncreaseBy).addComponent(txtIncreaseBy).addComponent(lblMs)
								.addComponent(btnIncrease).addGap(0, 0, Short.MAX_VALUE).addComponent(hypValidation)
								.addGap(0, 0, Short.MAX_VALUE).addComponent(btnFirst).addComponent(btnPrev)
								.addComponent(txtCurrentPage).addComponent(btnNext).addComponent(btnLast))
				.addGroup(layout.createSequentialGroup().addComponent(scrollPane)));

		layout.setVerticalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup(Alignment.BASELINE).addComponent(lblFilename)
								.addComponent(txtFilename).addComponent(btnLoadSubtitlesFile).addComponent(btnSave)
								.addComponent(chkBackup))
				.addGroup(

						layout.createParallelGroup(Alignment.BASELINE).addComponent(lblSelected)
								.addComponent(txtSelected).addComponent(lblIncreaseBy).addComponent(txtIncreaseBy)
								.addComponent(lblMs).addComponent(btnIncrease).addComponent(hypValidation)
								.addComponent(btnFirst).addComponent(btnPrev).addComponent(txtCurrentPage)
								.addComponent(btnNext).addComponent(btnLast))
				.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(scrollPane)));

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle(getString("mainTitle"));
		setMinimumSize(new Dimension(1024, 768));

		addComponentListener(new ComponentAdapter()
		{

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ComponentAdapter#componentResized(java.awt.event
			 * .ComponentEvent)
			 */
			@Override
			public void componentResized(ComponentEvent e)
			{
				Dimension d = getSize();
				Dimension minD = getMinimumSize();

				if(d.width < minD.width)
				{
					d.width = minD.width;
				}

				if(d.height < minD.height)
				{
					d.height = minD.height;
				}

				setSize(d);
			}
		});

		addWindowListener(new DefaultWindowListener(this));
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{

			/**
			 * Run.
			 */
			@Override
			public void run()
			{
				try
				{
					new SRTEditorMain().setVisible(true);
				}
				catch(IOException ex)
				{
					ex.printStackTrace();
				}
			}
		});
	}

	/**
	 * Txt increase by_on document change.
	 *
	 * @param document
	 *            the document
	 */
	private void txtIncreaseBy_onDocumentChange(Document document)
	{
		String regex1 = "(-?)[0-9]{1,3}(,[0-9]{3,3})*";
		String regex2 = "(-?)([0-9])*";
		String text = "";

		try
		{
			text = document.getText(0, document.getLength());
		}
		catch(BadLocationException ex)
		{
			ex.printStackTrace();
		}

		boolean isInteger = text.matches(regex1) || text.matches(regex2);
		long value = 0;
		if(isInteger && !text.isEmpty())
		{
			text = text.replace(",", "");
			value = Long.parseLong(text);
		}

		btnIncrease.setEnabled(isInteger && value != 0 && document.getLength() > 0);
	}

	/**
	 * Inits the menu bar.
	 */
	private void initMenuBar()
	{
		setJMenuBar(new JMenuBar());

		JMenuItem menuItemFileLoadSubtitlesFile = new JMenuItem();
		menuItemFileLoadSubtitlesFile.setText(getString("menuItemFileLoadSubtitlesFile"));
		menuItemFileLoadSubtitlesFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		menuItemFileLoadSubtitlesFile.addActionListener(new ActionListener()
		{

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			@Override
			public void actionPerformed(ActionEvent event)
			{
				loadSubtitlesFile();
			}
		});

		JMenuItem menuItemFileExit = new JMenuItem();
		menuItemFileExit.setText(getString("menuItemFileExit"));
		menuItemFileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		menuItemFileExit.addActionListener(new ActionListener()
		{

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			@Override
			public void actionPerformed(ActionEvent event)
			{
				close();
			}
		});

		JMenu menuFile = new JMenu();
		menuFile.setText(getString("menuFile"));
		menuFile.setMnemonic('F');
		menuFile.add(menuItemFileLoadSubtitlesFile);
		menuFile.add(new JSeparator());
		menuFile.add(menuItemFileExit);

		getJMenuBar().add(menuFile);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.magriso.common.Closable#close()
	 */
	@Override
	public void close()
	{
		String popupMessage = "confirmExitMsg";
		if(srtObject != null && srtObject.isDirty())
		{
			popupMessage = "confirmExitNoSaveMsg";
		}

		if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this, getString(popupMessage),
				getString("confirmExitTitle"), JOptionPane.YES_NO_OPTION))
		{
			dispose();
		}
	}

	/**
	 * Load subtitles file.
	 */
	private void loadSubtitlesFile()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileFilter()
		{

			/*
			 * (non-Javadoc)
			 * 
			 * @see javax.swing.filechooser.FileFilter#getDescription()
			 */
			@Override
			public String getDescription()
			{
				return "SRT Files";
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
			 */
			@Override
			public boolean accept(File file)
			{
				return file.getName().toLowerCase().endsWith(".srt") || file.isDirectory();
			}
		});

		if(!txtFilename.getText().isEmpty())
		{
			fileChooser.setSelectedFile(new File(txtFilename.getText()));
		}

		if(JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(getRootPane()))
		{
			String filename = fileChooser.getSelectedFile().getAbsolutePath();
			txtFilename.setText(filename);
			txtFilename.setToolTipText(filename);

			try
			{
				srtObject = new SRTObject(fileChooser.getSelectedFile());
			}
			catch(IOException ex)
			{
				throw new RuntimeException(ex);
			}

			txtSelected.setText("");
			displayLoadedSubtitles();
			handlePaginationControls(srtObject.getSize());
			txtIncreaseBy.setEditable(true);
			txtIncreaseBy.setText("0");

			hypValidation.setEnabled(true);

			pack();
		}
	}

	/**
	 * Handle pagination controls.
	 *
	 * @param numOfEntries
	 *            the num of entries
	 */
	private void handlePaginationControls(int numOfEntries)
	{
		if(numOfEntries != txtCurrentPage.getNumOfEntries())
		{
			txtCurrentPage.setNumOfEntries(numOfEntries);
		}

		int currentPageNum = txtCurrentPage.getCurrentPageNum();
		btnFirst.setEnabled(currentPageNum > 1);
		btnPrev.setEnabled(currentPageNum > 1);

		int numOfPages = txtCurrentPage.getNumOfPages();
		btnNext.setEnabled(numOfPages > 1 && currentPageNum < numOfPages);
		btnLast.setEnabled(numOfPages > 1 && currentPageNum < numOfPages);
	}

	/**
	 * Display loaded subtitles.
	 */
	private void displayLoadedSubtitles()
	{
		displayLoadedSubtitles(1);

		srtObjectTable.getSelectionModel().setAnchorSelectionIndex(0);
		srtObjectTable.getSelectionModel().setLeadSelectionIndex(0);
	}

	/**
	 * Display loaded subtitles.
	 *
	 * @param newPageNum
	 *            the new page num
	 */
	private void displayLoadedSubtitles(int newPageNum)
	{
		Object[][] tableData = srtObject.to2DimensionsArray((newPageNum - 1) * PaginationTextField.DEFAULT_PAGE_SIZE,
				PaginationTextField.DEFAULT_PAGE_SIZE);
		TableModel model = new DefaultTableModel(tableData, columnNames);

		if(singleSelectSelectionListener != null)
		{
			srtObjectTable.getSelectionModel().removeListSelectionListener(singleSelectSelectionListener);
		}

		srtObjectTable.setModel(model);
		handleTableDefaults();
	}

	/**
	 * Handle table defaults.
	 */
	private void handleTableDefaults()
	{
		srtObjectTable.setRowHeight(40);
		singleSelectSelectionListener = new SingleSelectSelectionListener(txtSelected, srtObjectTable);
		srtObjectTable.getSelectionModel().addListSelectionListener(singleSelectSelectionListener);
		srtObjectTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		for(int indx = 0; indx < columnNames.length; indx++)
		{
			TableColumn currentColumn = srtObjectTable.getColumnModel().getColumn(indx);

			switch(indx)
			{
				case 0:
					currentColumn.setPreferredWidth(50);
					break;

				case 1:
				case 2:
					currentColumn.setPreferredWidth(200);
					break;

				case 3:
					currentColumn.setCellRenderer(new TextAreaRenderer());
					currentColumn.setCellEditor(new TextAreaEditor());
					currentColumn.setPreferredWidth(500);
					break;
			}
		}
	}

	/**
	 * Go to next page.
	 */
	public void goToNextPage()
	{
		txtCurrentPage.goToNextPage();
		updateControlsForNewPage();
	}

	/**
	 * Go to previous page.
	 */
	public void goToPreviousPage()
	{
		txtCurrentPage.goToPreviousPage();
		updateControlsForNewPage();
	}

	/**
	 * Go to first page.
	 */
	public void goToFirstPage()
	{
		txtCurrentPage.goToFirstPage();
		updateControlsForNewPage();
	}

	/**
	 * Go to last page.
	 */
	public void goToLastPage()
	{
		txtCurrentPage.goToLastPage();
		updateControlsForNewPage();
	}

	/**
	 * Update controls for new page.
	 */
	private void updateControlsForNewPage()
	{
		int currentPageNum = txtCurrentPage.getCurrentPageNum();

		scrollPane.getVerticalScrollBar().setValue(0);
		displayLoadedSubtitles(currentPageNum);
		handlePaginationControls(srtObject.getSize());

		int selected = Integer.parseInt(txtSelected.getText());
		if(selected >= (currentPageNum - 1) * PaginationTextField.DEFAULT_PAGE_SIZE - 1
				&& selected < currentPageNum * PaginationTextField.DEFAULT_PAGE_SIZE)
		{
			ListSelectionModel selectionModel = srtObjectTable.getSelectionModel();
			selectionModel.setAnchorSelectionIndex(0);
			selectionModel.setLeadSelectionIndex(selected - (currentPageNum - 1)
					* PaginationTextField.DEFAULT_PAGE_SIZE - 1);
		}
	}

	/**
	 * Handle time increase.
	 */
	private void handleTimeIncrease()
	{
		String startFrom = txtSelected.getText();
		String increaseBy = txtIncreaseBy.getText().replaceAll(",", "");
		if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this,
				String.format(getString("confirmIncreaseByMsg"), increaseBy, startFrom),
				getString("confirmIncreaseByTitle"), JOptionPane.YES_NO_OPTION))
		{
			srtObject.add(Long.parseLong(increaseBy), Integer.parseInt(startFrom) - 1);
			txtIncreaseBy.setText("0");
			btnSave.setEnabled(true);
			chkBackup.setEnabled(true);
			displayLoadedSubtitles(txtCurrentPage.getCurrentPageNum());
		}
	}

	/**
	 * Save updated srt file.
	 */
	private void saveUpdatedSrtFile()
	{
		btnSave.setEnabled(false);
		chkBackup.setEnabled(false);

		boolean backupSuccessful = false;
		if(chkBackup.isSelected())
		{
			backupSuccessful = backupBeforeSave();
		}

		if(!chkBackup.isSelected() || backupSuccessful)
		{
			try(OutputStream outStream = new FileOutputStream(txtFilename.getText());
					Writer writer = new OutputStreamWriter(outStream, "UTF-8");)
			{
				writer.write(srtObject.toString());
			}
			catch(IOException ex)
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), getString("saveUpdatedFileErrorTitle"),
						JOptionPane.ERROR_MESSAGE);
				btnSave.setEnabled(true);
			}

			if(!btnSave.isEnabled())
			{
				srtObject.markAllClean();
			}
		}
		else
		{
			btnSave.setEnabled(true);
			chkBackup.setEnabled(true);
		}
	}

	/**
	 * Backup before save.
	 *
	 * @return true, if successful
	 */
	private boolean backupBeforeSave()
	{
		String backupFile = txtFilename.getText() + ".bak";
		boolean backupSuccessful = true;
		try
		{
			Files.move(new File(txtFilename.getText()).toPath(), new File(backupFile).toPath(),
					StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
		}
		catch(IOException ex)
		{
			backupSuccessful = false;
			JOptionPane.showMessageDialog(this, ex.getMessage(), getString("saveBackupErrorTitle"),
					JOptionPane.ERROR_MESSAGE);
		}

		return backupSuccessful;
	}
}
