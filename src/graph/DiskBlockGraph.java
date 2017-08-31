package graph;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * 此为磁盘块视图
 * 其中灰色表示未分配的可以分配的磁盘块，蓝色的表示已经分配的已用磁盘块
 */

public class DiskBlockGraph extends GridPane {

	private int row;	//磁盘块的行数
	private int column;	//磁盘块的列数
	private int totalNum;	//总块数
	private static final double BLOCK_WIDTH = 26;	//磁盘块的宽度
	private static final double BLOCK_HEIGHT = 18;	//磁盘块的高度
	private List<Label> diskBlockList = new ArrayList<>();	//磁盘块集合

	//构造函数
	public DiskBlockGraph() {
		//默认10行10列
		this(10, 10);
	}

	public DiskBlockGraph(int row, int column) {
		//接收参数
		this.row = row;
		this.column = column;
		this.totalNum = row * column;
		//初始化视图
		initBlockGraph();
		//分配前三块
		allocateDiskBlock(0);
		allocateDiskBlock(1);
		allocateDiskBlock(2);
	}

	//初始化磁盘块视图
	private void initBlockGraph() {
		//添加磁盘块
		for(int i = 0; i < column; i++) {
			for(int j = 0; j < row; j++) {
				//创建磁盘块
				Label diskBlock = new Label();
				//编号
				int value = i * row + j;
				diskBlock.setText(String.valueOf(value));
				//为磁盘块设置样式
				diskBlock.setAlignment(Pos.CENTER);
				diskBlock.setPrefSize(BLOCK_WIDTH, BLOCK_HEIGHT);
				this.setFreeStyle(diskBlock);
				//添加此磁盘块视图中
				this.add(diskBlock, j, i);
				//添加到磁盘块集合
				diskBlockList.add(diskBlock);
			}
		}
		//背景设置为白色
		this.setStyle("-fx-background-color:#FFFFFF;");
		//居中
		this.setAlignment(Pos.CENTER);
	}

	//分配一块磁盘块
	public void allocateDiskBlock(int position) {
		//检测位置是否合法
		if (position >= 0 && position < totalNum) {
			//获取对应位置的磁盘对象
			Label block = diskBlockList.get(position);
			//改变磁盘块样式
			this.setAllocatedStyle(block);
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("错误");
			alert.setHeaderText(null);
			alert.setContentText("磁盘块不存在，无法完成分配\n请输入正确的磁盘块号");
			alert.showAndWait();
		}
	}

	//分配一组磁盘块
	public void allocateDiskBlocks(List<Integer> positionList) {
		if (positionList != null && positionList.size() > 0) {
			for(int i = 0; i < positionList.size(); i++) {
				//逐块分配磁盘
				allocateDiskBlock(i);
			}
		}
	}

	//未分配子项的样式
	private void setFreeStyle(Label block) {
		block.setStyle("-fx-background-color:#D0D0D0; -fx-border-width:1.5; -fx-border-color:#FFFFFF;");
	}

	//已分配子项的样式
	private void setAllocatedStyle(Label block) {
		block.setStyle("-fx-background-color:#87CEFA; -fx-border-width:1.5; -fx-border-color:#FFFFFF;");
	}

}
