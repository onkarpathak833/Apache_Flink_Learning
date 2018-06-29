package Flink.Demo;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.twitter.TwitterSource;
import org.apache.flink.util.Collector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TwitterStreaming {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ParameterTool params = ParameterTool.fromArgs(args);
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.getConfig().setGlobalJobParameters(params);
		if(params.has(TwitterSource.CONSUMER_KEY)){
			
		}
		
		DataStream<String> twitterStream = env.addSource(new TwitterSource(params.getProperties()));
		
		DataStream<Tuple2<String, Integer>> stream = twitterStream.flatMap(new TwitterFlatMap()).keyBy(0).window(TumblingProcessingTimeWindows.of(Time.seconds(10))).sum(1);
		stream.print();
		env.execute();
	}
	
	@SuppressWarnings("serial")
	public static class TwitterFlatMap implements FlatMapFunction<String, Tuple2<String, Integer>>{

		
		private ObjectMapper jsonValue;
		public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
			// TODO Auto-generated method stub
			if(jsonValue==null){
				jsonValue = new ObjectMapper();
			}
			JsonNode jsonNode = jsonValue.readValue(value, JsonNode.class);
			String language ="";
			if(jsonNode.has("user")){
				if(jsonNode.get("user").has("lang")) {
					language = jsonNode.get("user").get("lang").asText();
				}
				else {
					language = "unknown";
				}
			}
		}
		
		
		
	}
	
}
