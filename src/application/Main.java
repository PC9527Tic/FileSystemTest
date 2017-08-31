package application;

import graph.DiskBlockGraph;
import graph.DiskPieGraph;
import graph.FATGraph;
import graph.FileManagerGraph;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {

	private DiskPieGraph diskPieGraph;	//扇形磁盘分配表
	private static Stage stage;	//显示窗口

	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			HBox root = new HBox();
			root.setPadding(new Insets(10));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			//添加文件管理窗口
			FileManagerGraph managerGraph = new FileManagerGraph();
			root.getChildren().add(managerGraph);

			//创建扇形磁盘分配表
			diskPieGraph = new DiskPieGraph(3, 128);

			//添加磁盘文件块分配表
			DiskBlockGraph diskBlockGraph = new DiskBlockGraph(16, 8);
			VBox blockBox = new VBox();
			blockBox.setStyle("-fx-background-color:#FFFFFF;");
			blockBox.setAlignment(Pos.CENTER);
			blockBox.setPadding(new Insets(10, 0, 10, 10));
			blockBox.setSpacing(30);
			Label title = new Label("磁盘分配情况");
			title.setFont(Font.font("宋体", 16));
			title.setTextFill(Color.CORNFLOWERBLUE);
			blockBox.getChildren().addAll(title, diskBlockGraph);
			HBox diskGraphBox = new HBox();
			diskGraphBox.getChildren().addAll(blockBox, diskPieGraph);
			managerGraph.getEditBox().getChildren().add(diskGraphBox);

			//添加文件配置表
			FATGraph fatGraph = FATGraph.getInstance();
			fatGraph.prefHeightProperty().bind(managerGraph.getEditBox().heightProperty());
			root.getChildren().add(fatGraph);

			//添加文件目录树
			//FileDirectoryTreeGraph fileGraph = new FileDirectoryTreeGraph();
			//root.setTop(fileGraph);

			//改变扇形图的按钮
			HBox hbox = new HBox();
			TextField allocateFiled = new TextField();
			allocateFiled.setPromptText("输入要分配的磁盘块");
			Button allocateButton = new Button("确定");
			allocateButton.setOnMouseClicked(e->{
				//获取分配磁盘块的位置
				int position = Integer.parseInt(allocateFiled.getText());
				//分配磁盘块
				diskBlockGraph.allocateDiskBlock(position);
			});
			TextField changeFiled = new TextField();
			changeFiled.setPromptText("输入改变值");
			Button changeButton = new Button("确定");
			changeButton.setOnMouseClicked(e->{
				//获取改变值
				double value =Double.parseDouble(changeFiled.getText());
				//根据改变值更新扇形图
				diskPieGraph.updatePieGraph(value);
			});
			hbox.setSpacing(5);
			hbox.getChildren().addAll(allocateFiled, allocateButton, changeFiled, changeButton);

			primaryStage.setTitle("FileSystem");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//获取窗口对象
	public static Stage getStage() {
		return stage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
