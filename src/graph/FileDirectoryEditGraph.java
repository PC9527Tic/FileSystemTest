package graph;

import file.MyFile;
import graph.FileDirectoryTreeGraph.MyTreeItem;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * 此类为构建文件目录管理编辑的可视化窗口
 *
 */

public class FileDirectoryEditGraph extends FlowPane {

	//图标
	private String fileIconSrc = "/image/file96.png";
	private String emptyFolderIconSrc = "/image/emptyFolder96.png";
	private String folderIconSrc = "/image/folder96.png";
	//视图大小
	private static final int VIEW_WIDTH = 400;
	private static final int VIEW_HEIGHT = 400;
	//间距大小
	private static final int GAP_SIZE = 5;
	//选中背景颜色和默认的颜色
	private static final String SELECTED_COLOR = "#E0FFFF";
	private static final String DEFAULT_COLOR = "#FFFFFF";
	//右键菜单
	private EditGraphMenu addMenu;
	private boolean exist = false;

	public FileDirectoryEditGraph() {
		//初始化窗口
		initGraph();
	}

	//初始化窗口
	public void initGraph() {
		//创建右键添加菜单
		this.addMenu = new EditGraphMenu();
		//设置右键显示菜单
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e)->{
			//如果是鼠标右键点击或者control键，则显示右键菜单；否则，隐藏
			if (!exist && (e.getButton() == MouseButton.SECONDARY  || e.isControlDown()))  {
				if(this.addMenu.isShowing()) {
					this.addMenu.hide();
				}
				this.addMenu.show(this, e.getScreenX(), e.getScreenY());
			}  else  {
				this.addMenu.hide();
			}
		});
		
		//设置窗口大小
		this.setPrefSize(VIEW_WIDTH, VIEW_HEIGHT);
		//设置间距
		this.setHgap(GAP_SIZE);
		this.setVgap(GAP_SIZE);
		//设置背景色
		this.setStyle("-fx-background-color:#FFFFFF; -fx-border-width:0.5; -fx-border-color:#DADADA;");
		//设置间距
		this.setPadding(new Insets(5));
	}
	
	//获取右键菜单
	public EditGraphMenu getAddMenu() {
		return this.addMenu;
	}

	//新建文件或者文件夹
	public FileDirectoryItem addFileDirectory(int attribute) {
		//创建文件或者文件夹子项
		FileDirectoryItem item = new FileDirectoryItem(attribute, this);
		//添加
		this.getChildren().add(item);
		//返回
		return item;
	}

	//添加已有文件或者文件夹
	public FileDirectoryItem addFileDirectory(MyTreeItem treeItem) {
		//创建文件或者文件夹子项
		FileDirectoryItem item = new FileDirectoryItem(treeItem, this);
		//添加
		this.getChildren().add(item);
		//返回
		return item;
	}

	//删除指定的已有文件或者文件夹
	public void removeFileDirectory(MyTreeItem treeItem) {
		//获取子项集合
		ObservableList<Node> itemList = this.getChildren();
		//删除指定子项
		if (itemList != null && itemList.size() > 0) {
			for(Node node : itemList) {
				FileDirectoryItem item = (FileDirectoryItem) node;
				if(item.getTreeItem() == treeItem) {
					//如果要删除的目录树子项与子项相关联的目录树子项一致，则删除
					this.getChildren().remove(node);
					//退出
					break;
				}
			}
		}
	}
	
	//编辑窗口右键菜单
	class EditGraphMenu extends ContextMenu {
		
		private MenuItem addFile;	//新建文件菜单项
		private MenuItem addFolder;	//新建文件夹菜单项
		
		//构造方法
		public EditGraphMenu() {
			//初始化
			this.initMenu();
		}
		
		//初始化菜单项
		private void initMenu() {
			//创建菜单项
			addFile = new MenuItem("新建文件");
			addFolder = new MenuItem("新建文件夹");
			//添加菜单项到菜单栏
			this.getItems().addAll(addFile, addFolder);
		}
		
		//获取菜单项
		public MenuItem getAddFile() {
			return addFile;
		}
		public MenuItem getAddFolder() {
			return addFolder;
		}
		
	}

	//文件或者文件夹
	class FileDirectoryItem extends VBox {

		private TextField renameField;	//重命名文件时输入框
		private Label name;	//文件或文件夹名称
		private Label icon;	//图标
		private int attribute;	//属性
		private FileDirectoryMenu menu;	//右键菜单
		private FileDirectoryItem item;	//实例
		private MyTreeItem treeItem;	//文件目录树子项
		private Node parent;	//所处的窗口

		//构造函数	-- 用于新建文件或者文件夹（接收参数为文件类型属性）
		public FileDirectoryItem(int attribute, Node parent) {
			//接收参数
			this.attribute = attribute;
			this.parent = parent;
			//初始化
			initItem();
			//指向当前对象
			item = this;
		}

		//构造函数 -- 用于往布局中添加已存在的文件或者文件夹（接受参数为文件目录树子项）
		public FileDirectoryItem(MyTreeItem treeItem, Node parent) {
			//接收参数
			this.treeItem = treeItem;
			this.attribute = treeItem.getAttribute();
			this.parent = parent;
			//初始化
			initItem();
			//指向当前对象
			item = this;
		}

		//获取重命名框
		public TextField getRenameFiled() {
			return this.renameField;
		}

		//获取名称
		public Label getName() {
			return this.name;
		}

		//获取相关联的目录树子项
		public MyTreeItem getTreeItem() {
			return this.treeItem;
		}
		
		//获取右键菜单
		public FileDirectoryMenu getMenu() {
			return this.menu;
		}
		
		//设置相关联的目录树子项
		public void setTreeItem(MyTreeItem treeItem) {
			this.treeItem = treeItem;
		}

		//初始化实例
		private void initItem() {

			icon = new Label("");
			name = new Label();//文本名称
			//设置显示名称的字体和大小
			name.setFont(Font.font("宋体", 12));
			name.setAlignment(Pos.CENTER);
			name.setPrefWidth(90);
			name.setWrapText(true);
			//新创建时，隐藏name，显示重命名框
			renameField = new TextField();	//重命名框
			//设置名字文本的字体和大小
			renameField.setFont(Font.font("宋体", 12));
			renameField.setAlignment(Pos.CENTER);
			//设置为可编辑
			renameField.setEditable(true);
			renameField.setPrefWidth(90);
			//保存重命名
			renameField.setOnAction(e->{
				System.out.println("保存");
				//显示名称
				name.setText(renameField.getText());
				this.getChildren().add(name);
				//目录结点名称更新
				treeItem.setValue(renameField.getText());
				//移除重命名框
				this.getChildren().remove(renameField);
			});
			
			//设置名称
			if (treeItem != null) {
				//如果目录子项不为空，则设置的信息来自子项
				name.setText(treeItem.getValue());
				renameField.setText(treeItem.getValue());
				//添加图标和名称
				this.getChildren().addAll(icon, name);
			} else {
				//创建目录树子项
				this.treeItem = new MyTreeItem(attribute);
				//根据创建实例的不同，设置不同的信息
				if (attribute == MyFile.FILE_VALUE) {
					//设置文本信息
					name.setText("新建文件.txt");
					renameField.setText("新建文件.txt");
					//添加图标和重命名框
					this.getChildren().addAll(icon, renameField);
				} else if(attribute == MyFile.FOLDER_VALUE) {
					//设置文本信息
					name.setText("新建文件夹");
					renameField.setText("新建文件夹");
					//添加图标和重命名框
					this.getChildren().addAll(icon, renameField);
				}
			}
			
			//设置图标
			if (attribute == MyFile.FILE_VALUE) {
				ImageView fileIcon = new ImageView(new Image(getClass().getResourceAsStream(fileIconSrc)));	//文件图标
				this.icon.setGraphic(fileIcon);
			} else if(attribute == MyFile.FOLDER_VALUE) {
				ImageView emptyIcon = new ImageView(new Image(getClass().getResourceAsStream(emptyFolderIconSrc)));	//空文件夹图标
				ImageView normalIcon = new ImageView(new Image(getClass().getResourceAsStream(folderIconSrc)));	//标准文件夹图标
				//根据是不是叶子结点判断是否为空文件夹，设置不同图标
				if (treeItem.isLeaf()) {
					this.icon.setGraphic(emptyIcon);
				} else {
					this.icon.setGraphic(normalIcon);
				}
				this.treeItem.leafProperty().addListener(e->{
					if (treeItem.isLeaf()) {
						//如果是空文件夹，设置空文件夹图标
						this.icon.setGraphic(emptyIcon);
					} else {
						//如果不是空文件夹，设置正常文件夹图标
						this.icon.setGraphic(normalIcon);
					}
				});
			}

			//设置居中显示
			this.setAlignment(Pos.CENTER);
			//设置间距
			this.setSpacing(5);
			this.setPadding(new Insets(0, 0, 5, 0));
			//设置默认颜色
			this.setStyle("-fx-background-color:"+DEFAULT_COLOR+";");
			name.setStyle("-fx-background-color:"+DEFAULT_COLOR+";");
			//设置选中颜色
			this.setOnMouseEntered(e->{
				this.setStyle("-fx-background-color:"+SELECTED_COLOR+";");
				name.setStyle("-fx-background-color:"+SELECTED_COLOR+";");
			});
			this.setOnMouseExited(e->{
				this.setStyle("-fx-background-color:"+DEFAULT_COLOR+";");
				name.setStyle("-fx-background-color:"+DEFAULT_COLOR+";");
			});
			//设置菜单
			menu = new FileDirectoryMenu();
			this.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e)->{
				if (e.getButton() == MouseButton.SECONDARY  || e.isControlDown())  {
					if(menu.isShowing()) {
						menu.hide();
					}
					menu.show(this, e.getScreenX(), e.getScreenY());
				}  else  {
					menu.hide();
				}
			});
			menu.showingProperty().addListener(e->{
				if (menu.isShowing()) {
					exist = true;
				} else {
					exist = false;
				}
			});
			
			//点击重命名框之外的地方，保存重命名
			this.parent.setOnMouseClicked(e->{
				if (this.getChildren().contains(renameField)) {
					if (!renameField.isHover()) {
						//显示名称
						name.setText(renameField.getText());
						this.getChildren().add(name);
						//目录结点名称更新
						treeItem.setValue(renameField.getText());
						//移除重命名框
						this.getChildren().remove(renameField);
					}
				}
			});
			
		}

		//右键菜单
		class FileDirectoryMenu extends ContextMenu {

			private MenuItem open;	//打开菜单项
			private MenuItem delete;	//删除菜单项
			private MenuItem rename;	//重命名菜单项
			private MenuItem attribute;	//属性菜单项

			//构造函数
			public FileDirectoryMenu() {
				//初始化菜单
				initMenu();
			}

			//初始化菜单
			private void initMenu() {
				//创建菜单项
				open = new MenuItem("打开");
				delete = new MenuItem("删除");
				rename = new MenuItem("重命名");
				attribute = new MenuItem("属性");
				//添加菜单项到菜单
				this.getItems().addAll(open, delete, rename, attribute);
				//为菜单项设置事件
				rename.setOnAction(e->{
					//添加重命名框
					item.getChildren().add(item.renameField);
					item.renameField.setText(name.getText());
					//移除名称
					item.getChildren().remove(item.name);
				});
			}
			
			//获取菜单项

			public MenuItem getOpen() {
				return open;
			}

			public MenuItem getDelete() {
				return delete;
			}

			public MenuItem getRename() {
				return rename;
			}

			public MenuItem getAttribute() {
				return attribute;
			}

		}

	}




}
