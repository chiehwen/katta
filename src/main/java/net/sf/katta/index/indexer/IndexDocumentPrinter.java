package net.sf.katta.index.indexer;

import net.sf.katta.index.indexer.merge.DfsIndexDirectory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobConf;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.MapFieldSelector;
import org.apache.lucene.index.IndexReader;

public class IndexDocumentPrinter {

  public static void main(String[] args) throws Exception {

    Options options = new Options();
    options.addOption("hadoopConf", true, "Hadoop Site Xml");
    options.addOption("shard", true, "Index Shard (Zip File)");
    options.addOption("workingPath", true, "Working Path in dfs for shard extraction");
    options.addOption("indexField", true, "index field to print out");

    CommandLineParser parser = new PosixParser();
    CommandLine cmd = parser.parse(options, args);

    if (!cmd.hasOption("hadoopConf") || !cmd.hasOption("shard") || !cmd.hasOption("workingPath") || !cmd.hasOption("indexField")) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(IndexDocumentPrinter.class.getName(), options);
      return;
    }

    String hadoopConf = cmd.getOptionValue("hadoopConf");
    Path zipFile = new Path(cmd.getOptionValue("shard"));
    Path workingPath = new Path(cmd.getOptionValue("workingPath"), "" + System.currentTimeMillis());

    String indexField = cmd.getOptionValue("indexField");


    JobConf jobConf = new JobConf(hadoopConf);
    FileSystem fileSystem = FileSystem.get(jobConf);
    fileSystem.mkdirs(workingPath);

    DfsIndexDirectory directory = new DfsIndexDirectory(fileSystem, zipFile, workingPath);
    IndexReader reader = IndexReader.open(directory);
    int maxDoc = reader.maxDoc();
    MapFieldSelector selector = new MapFieldSelector(new String[]{indexField});
    for (int i = 0; i < maxDoc; i++) {
      Document document = reader.document(i, selector);
      String indexValue = document.get(indexField);
      System.out.println("doc [" + i + "] # " + indexField + ": " + indexValue);

    }

    System.out.println("delete workingPath " + workingPath);
    fileSystem.delete(workingPath);
    System.out.println("");
    System.out.println("");

    System.out.println("Max. documents in shard: " + maxDoc);
  }
}