package ca.corefacility.bioinformatics.irida.service.impl.unit;

import ca.corefacility.bioinformatics.irida.config.services.IridaApiServicesConfig;
import ca.corefacility.bioinformatics.irida.model.run.SequencingRun;
import ca.corefacility.bioinformatics.irida.model.run.SequencingRun.LayoutType;
import ca.corefacility.bioinformatics.irida.model.sample.Sample;
import ca.corefacility.bioinformatics.irida.model.sequenceFile.SequenceFile;
import ca.corefacility.bioinformatics.irida.model.sequenceFile.SequenceFilePair;
import ca.corefacility.bioinformatics.irida.model.sequenceFile.SingleEndSequenceFile;
import ca.corefacility.bioinformatics.irida.repositories.filesystem.IridaFileStorageService;
import ca.corefacility.bioinformatics.irida.repositories.joins.sample.SampleSequencingObjectJoinRepository;
import ca.corefacility.bioinformatics.irida.repositories.sequencefile.SequenceConcatenationRepository;
import ca.corefacility.bioinformatics.irida.repositories.sequencefile.SequenceFileRepository;
import ca.corefacility.bioinformatics.irida.repositories.sequencefile.SequencingObjectRepository;
import ca.corefacility.bioinformatics.irida.service.SequencingObjectService;
import ca.corefacility.bioinformatics.irida.service.impl.SequencingObjectServiceImpl;
import ca.corefacility.bioinformatics.irida.web.controller.test.unit.TestDataFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.Validator;
import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { IridaApiServicesConfig.class })
@ActiveProfiles("it")
public class SequencingObjectServiceTest {

	SequencingObjectService service;
	SequencingObjectRepository repository;
	SequenceFileRepository sequenceFileRepository;
	SampleSequencingObjectJoinRepository ssoRepository;
	SequenceConcatenationRepository concatenationRepository;
	Validator validator;

	@Autowired
	private IridaFileStorageService iridaFileStorageService;

	@Before
	public void setUp() {
		repository = mock(SequencingObjectRepository.class);
		sequenceFileRepository = mock(SequenceFileRepository.class);
		ssoRepository = mock(SampleSequencingObjectJoinRepository.class);

		concatenationRepository = mock(SequenceConcatenationRepository.class);

		service = new SequencingObjectServiceImpl(repository, sequenceFileRepository, ssoRepository,
				concatenationRepository, validator, iridaFileStorageService);
	}

	@Test
	public void testCreateSequenceFileInSample() throws IOException {
		Sample s = new Sample();

		SingleEndSequenceFile sf = TestDataFactory.constructSingleEndSequenceFile();

		when(repository.save(sf)).thenReturn(sf);

		service.createSequencingObjectInSample(sf, s);

		verify(sequenceFileRepository, times(1)).save(any(SequenceFile.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateSequenceFileInSampleWrongType() throws IOException {
		Sample s = new Sample();
		SingleEndSequenceFile so = TestDataFactory.constructSingleEndSequenceFile();
		SequencingRun run = new SequencingRun(SequencingRun.LayoutType.PAIRED_END, "miseq");

		so.setSequencingRun(run);

		when(repository.save(so)).thenReturn(so);

		service.createSequencingObjectInSample(so, s);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateSequenceFilePairInSampleWrongType() throws IOException {
		Sample s = new Sample();

		SequencingRun run = new SequencingRun(LayoutType.SINGLE_END, "miseq");

		SequenceFilePair so = TestDataFactory.constructSequenceFilePair();
		so.setSequencingRun(run);

		when(repository.save(so)).thenReturn(so);

		service.createSequencingObjectInSample(so, s);
	}
}
