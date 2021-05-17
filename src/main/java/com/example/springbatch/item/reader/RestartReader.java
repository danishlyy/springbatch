package com.example.springbatch.item.reader;

import com.example.springbatch.pojo.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RestartReader implements ItemStreamReader<Product> {

    private Long curLine = 0L;
    private boolean restart = false;
    private ExecutionContext executionContext;

    private FlatFileItemReader<Product> flatFileItemReader = new FlatFileItemReader<>();

    public RestartReader() {
        flatFileItemReader.setResource(new ClassPathResource("restart.txt"));
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        DefaultLineMapper<Product> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSet -> {
            Product product = new Product();
            product.setId(fieldSet.readLong(0));
            product.setProductName(fieldSet.readString(1));
            product.setProductCode(fieldSet.readString(2));
            product.setProductCompany(fieldSet.readString(3));
            return product;
        });
        defaultLineMapper.afterPropertiesSet();
        flatFileItemReader.setLineMapper(defaultLineMapper);
    }

    @Override
    public Product read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        Product product = null;
        this.curLine++;
        if (restart){
            flatFileItemReader.setLinesToSkip(this.curLine.intValue()-1);
            restart = false;
            log.info("start read currentLine:{}",this.curLine);
        }
        flatFileItemReader.open(executionContext);
        product = flatFileItemReader.read();
        if (product != null && "10022".equals(product.getProductCode())){
            throw new RuntimeException("读取异常,产品code是："+product.getProductCode());
        }
        return product;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.executionContext = executionContext;
        if (executionContext.containsKey("curLine")){
            this.curLine = executionContext.getLong("curLine");
            this.restart = true;
        }else {
            this.curLine = 0L;
            executionContext.put("curLine",this.curLine);
            log.info("start read,currentLine:{}",this.curLine+1);
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.put("curLine",this.curLine);
        log.info("currentLine:{}",this.curLine);
    }

    @Override
    public void close() throws ItemStreamException {

    }
}
