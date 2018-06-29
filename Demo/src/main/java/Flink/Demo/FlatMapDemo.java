package Flink.Demo;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class FlatMapDemo {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Flink FlatMap Demo");
		ParameterTool params = ParameterTool.fromArgs(args);
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.getConfig().setGlobalJobParameters(params);
		DataStream<String> stream = null;
		if(params.has("filename")){
			stream = env.readTextFile(params.get("filename")).flatMap(new FlatMap());
		}
		else if(params.has("host") && params.has("port")){
			stream = env.socketTextStream(params.get("host"), params.getInt("port")).flatMap(new FlatMap());
		}
		else {
			System.out.println("Something went wrong.");
		}
		
		stream.print();
		env.execute();
	}
	
	
	public static class FlatMap implements FlatMapFunction<String, String>{

		public void flatMap(String input, Collector<String> out) throws Exception {
			// TODO Auto-generated method stub
			for(String word:input.split("\\s+")){
				out.collect(word);
			}
		}
		
		
	}
	
}
