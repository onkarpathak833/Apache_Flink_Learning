package FirstFlinkApp.FirstFlinkApp;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.operators.StreamingRuntimeContext;

import akka.dispatch.Filter;

public class FlinkApp {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		DataStream<Long> stream = env.socketTextStream("localhost", 9999).filter(new Filter()).map(new Map());
		System.out.println(stream.print());
		stream.print();
		try {
			env.execute("Test Program");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error in executing the dataflow.");
			e.printStackTrace();
		}
	}

	
	public static class Filter implements FilterFunction<String> {

		public boolean filter(String input) throws Exception {
			// TODO Auto-generated method stub
			try{
				Double.parseDouble(input.trim());
				return true;
			}catch(Exception e){
				System.out.println("This is not integer data");
			}
			return false;
		}
		
		
	}
	public static class Map implements MapFunction<String, Long>{

		public Long map(String input) throws Exception {
			// TODO Auto-generated method stub
			Double decimal = Double.parseDouble(input);
			return Math.round(decimal);
		}
		
	}
}
