package com.pdfextract.common;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import edu.isi.bmkeg.lapdf.controller.LapdfEngine;
import edu.isi.bmkeg.lapdf.model.ChunkBlock;
import edu.isi.bmkeg.lapdf.model.LapdfDocument;
import edu.isi.bmkeg.lapdf.model.PageBlock;
import edu.isi.bmkeg.lapdf.model.ordering.SpatialOrdering;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import lombok.val;

public class LapdfExtractStrategy implements LayoutExtractor {
	
	//LapdfDocument lapdf;
	

	public LapdfExtractStrategy() throws IOException {
		//this.lapdf = new LapdfDocument();
		//this.stack = new ArrayList<Set<String>>();
	}

	@Override
	public List<String> extractData(PDDocument document, Layout layout) throws Exception {
		Boolean sectionFound = false;
		String drlLocation = (String) layout.getLayoutExtractorDetails().getAdditionalParameters().get("layoutRules");
		File drlFile = new File("/mnt/c/Users/mm285e/Desktop/krishna/tabula-new/bmkeg-parent-master/lapdftext-master/" + drlLocation);
		List<String> chunks = new ArrayList<String>();
		
		//final File test = new File("/mnt/c/Users/mm285e/Desktop/ocr/conversions/D333232_USG_Aprvl.pdf");
		final File file = File.createTempFile("test", ".pdf");
		document.save(file);
		//document.close(); May be necessary???	
		LapdfEngine engine = new LapdfEngine();
		LapdfDocument lapdf = engine.blockifyFile(file);
		engine.classifyDocument(lapdf, drlFile);
		lapdf.convertToLapdftextXmlFormat();
		//lapdf.setPdfFile(test);
		//LapdfDocument lapdf = new LapdfDocument(test);
		//Set<String> sections = new HashSet<>();
		//sections.add(ChunkBlock.TYPE_BODY);
		
		//List<ChunkBlock> textChunks = new ArrayList<>();
		//lapdf.readAllChunkBlocks().stream().map
		for(ChunkBlock chunk : lapdf.readAllChunkBlocks()) {
			if(chunk.getType() == ChunkBlock.TYPE_BODY) {
				//chunks.add(chunk.readChunkText().replaceAll("\\?", ""));
				sectionFound = true;
			}
			
			if(sectionFound && chunk.getType() != ChunkBlock.TYPE_HEADER && chunk.getType() != ChunkBlock.TYPE_BODY) {
				chunks.add(chunk.readChunkText().replaceAll("\\?", ""));
			}
		}
		//val textChunks = lapdf.readClassifiedChunkBlocks(sections);
		/*textChunks.forEach(chunk -> {
			//System.out.println(chunk.readChunkText());
			Chunks.add(chunk.readChunkText());
		});*/
		file.delete();
		//engine.classifyDocument(lapdf, new File(drlFile));
		//engine.dump
		//engine.

		return chunks;
	}
}
