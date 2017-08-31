package graph;


import java.text.DecimalFormat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * FilePieGraph是使用扇形图来反映磁盘的使用情况
 */

public class DiskPieGraph extends Pane {

	private Data freeData = null;	//未分配的扇区
	private Data allocatedData = null;	//已分配的扇区
	private double totalValue;	//整个饼状图的总值
	private double allocatedValue;	//已经分配的扇形值
	private static final double CHART_WIDTH = 200;	//饼状图的宽度
	private static final double CHART_HEIGHT = 220;	//饼状图的高度
	
	public DiskPieGraph() {
		this(0, 100);
	}
	
	public DiskPieGraph(double allocatedValue, double totalValue) {
		//接收参数
		this.allocatedValue = allocatedValue;
		this.totalValue = totalValue;
		//初始化
		initPieGraph();
	}
	
	//初始化扇形图
	private void initPieGraph() {
		//初始状态，已分配的为3%，未分配的为97%
		freeData = new Data("可用空间", totalValue - allocatedValue);
		allocatedData = new Data("已用空间", allocatedValue);
		//创建扇形图所需数据
		ObservableList<Data> chartData = FXCollections.observableArrayList();
		//添加数据
		if(allocatedValue != 0) {
			//不为0，添加
			chartData.add(allocatedData);
		}
		chartData.add(freeData);
		//创建扇形图
		PieChart diskChart = new PieChart(chartData);
		//为扇形图指定名称
		//diskChart.setTitle("磁盘分配表");
		//指定扇形图大小
		diskChart.setPrefSize(CHART_WIDTH, CHART_HEIGHT);
		//将图例的位置置为右边，默认在下边
		diskChart.setLabelsVisible(false);
		diskChart.setLegendSide(Side.BOTTOM);
		//设置扇区开始的角度
		diskChart.setStartAngle(90);
		//添加饼状图
		this.getChildren().add(diskChart);
		
		//显示比例的标签
		Label rateInfo = new Label("");	
		//设置样式
		rateInfo.setTextFill(Color.MEDIUMSLATEBLUE);	
		rateInfo.setFont(Font.font("Arial", 16));
		//添加比例标签
		this.getChildren().add(rateInfo);
		
		//图例上的比例
		HBox rateBox = new HBox();
		DecimalFormat format = new DecimalFormat("#00.00");
		//已分配比例
		Label allocatedRate = new Label(String.valueOf(format.format(allocatedValue/totalValue*100) + "%"));
		allocatedRate.setTextFill(Color.valueOf("#77AAFF"));
		allocatedRate.setFont(Font.font("Arial", 14));
		//未分配比例
		Label freeRate = new Label(String.valueOf(format.format((totalValue - allocatedValue)/totalValue * 100)+"%"));
		freeRate.setTextFill(Color.valueOf("#A0A0A0"));
		freeRate.setFont(Font.font("Arial", 14));
		rateBox.getChildren().addAll(allocatedRate, freeRate);
		rateBox.setSpacing(30);
		rateBox.setTranslateX(CHART_WIDTH / 2 - 60);
		rateBox.setLayoutY(CHART_HEIGHT);
		//添加图例上比例
		this.getChildren().add(rateBox);
		
		//设置鼠标按下扇区显示扇区所在比例的点击事件
		for(int i = 0; i < chartData.size(); i++) {
			Data data = chartData.get(i);
			//点击扇形图，显示比例
			data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					//显示比例
					rateInfo.setTranslateX(event.getX() + CHART_WIDTH/2);
					rateInfo.setTranslateY(event.getY() + CHART_HEIGHT/2 - 10);
					double rateValue = (data.getPieValue() / totalValue) * 100;
					//保留两位小数（四舍五入）
					DecimalFormat format = new DecimalFormat("#.00");
					rateInfo.setText(String.valueOf(format.format(rateValue)) + "%");
					rateInfo.setVisible(true);
				}
			});
			//扇区改变比例变化
			chartData.get(i).pieValueProperty().addListener(e->{
				//扇区上的比例隐藏
				rateInfo.setVisible(false);
				//图例上比例更新
				freeRate.setText(String.valueOf(format.format((totalValue - allocatedValue)/totalValue * 100)+"%"));
				allocatedRate.setText(String.valueOf(format.format(allocatedValue/totalValue*100) + "%"));
			});
		}
		
		//背景设为白色
		this.setStyle("-fx-background-color:#FFFFFF");
	}
	
	//通过接收分配或者回收的资源来更新扇形图
	public void updatePieGraph(double value) {
		//重新设置数据
		allocatedValue += value;
		allocatedData.setPieValue(allocatedValue);
		freeData.setPieValue(totalValue - allocatedValue);
	}

}
