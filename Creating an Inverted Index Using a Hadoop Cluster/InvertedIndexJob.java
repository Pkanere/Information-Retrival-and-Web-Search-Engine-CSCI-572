import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class InvertedIndexJob{

  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, Text>{

    
    private Text word = new Text();
    private Text one = new Text();

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	
    	
      String[] result = value.toString().split("\\t",2);
     
      result[1] = result[1].replaceAll("[0-9|_|\\W]", " ");
      result[1] = result[1].toLowerCase();
      
      StringTokenizer itr = new StringTokenizer(result[1]);
      while (itr.hasMoreTokens()) {
        word.set(itr.nextToken());
        one.set(result[0]);
        context.write(word, one);
      }
    }
  }

  public static class IntSumReducer
       extends Reducer<Text,Text,Text,Text> {
    

    public void reduce(Text key, Iterable<Text> values,
                       Context context
                       ) throws IOException, InterruptedException {
    	
      HashMap<String, Integer> h=new HashMap<String, Integer>();
      int value=0;
      
      for (Text val : values)
      { String str=val.toString();

      	if( h.get(str)!=null && h!=null){
      		value=h.get(str);
      		h.put(str, ++value);
      		}else{
      		h.put(str, 1);
      		}
      }
      
      String o="";
      
      for (String name : h.keySet())  
          o=o+name+":"+h.get(name)+" ";
      context.write(key, new Text(o));
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "inverted index");
    job.setJarByClass(InvertedIndexJob.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}