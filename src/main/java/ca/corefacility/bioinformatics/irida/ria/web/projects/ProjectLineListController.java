package ca.corefacility.bioinformatics.irida.ria.web.projects;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.ImmutableList;

import ca.corefacility.bioinformatics.irida.model.joins.Join;
import ca.corefacility.bioinformatics.irida.model.project.Project;
import ca.corefacility.bioinformatics.irida.model.sample.Sample;
import ca.corefacility.bioinformatics.irida.model.sample.SampleMetadata;
import ca.corefacility.bioinformatics.irida.service.ProjectService;
import ca.corefacility.bioinformatics.irida.service.sample.SampleService;

@Controller @RequestMapping("/projects/{projectId}/linelist") public class ProjectLineListController {
	private static final Logger logger = LoggerFactory.getLogger(ProjectLineListController.class);

	private static final List<String> TEMPLATE = ImmutableList.of(
			"PFGE-XbaI-pattern",
			"PFGE-BlnI-pattern",
			"NLEP #",
			"SubmittedNumber",
			"Province",
			"SourceSite",
			"SourceType",
			"PatientAge",
			"PatientSex",
			"Genus",
			"Serotype",
			"ReceivedDate",
			"UploadDate",
			"IsolatDate",
			"SourceCity",
			"UploadModifiedDate",
			"Comments",
			"Outbreak",
			"Phagetype",
			"Traveled_To",
			"Exposure");

	private final ProjectService projectService;
	private final SampleService sampleService;
	private final ProjectControllerUtils projectControllerUtils;
	private MessageSource messageSource;

	@Autowired
	public ProjectLineListController(ProjectService projectService, SampleService sampleService,
			ProjectControllerUtils utils, MessageSource messageSource) {
		this.projectService = projectService;
		this.sampleService = sampleService;
		this.projectControllerUtils = utils;
		this.messageSource = messageSource;
	}

	@RequestMapping("")
	public String getLineListPage(@PathVariable Long projectId, Model model, Principal principal) {
		// Set up the template information
		Project project = projectService.read(projectId);
		projectControllerUtils.getProjectTemplateDetails(model, principal, project);
		model.addAttribute("activeNav", "linelist");
		// THIS IS WHERE THE TEMPLATE WOULD BE APPLIED
		model.addAttribute("headers", TEMPLATE);

		List<Join<Project, Sample>> projectSamples = sampleService.getSamplesForProject(project);
		List<List<Object>> metadata = new ArrayList<>();
		for (Join<Project, Sample> join : projectSamples) {
			Sample sample = join.getObject();
			SampleMetadata sampleMetadata = sampleService.getMetadataForSample(sample);
			// TODO: This is where the project template should be applied.  For now return everything.
			Map<String, Object> data;
			if(sampleMetadata != null) {
				data = sampleMetadata.getMetadata();
			} else {
				data = new HashMap();
			}
			List<Object> metalist = new ArrayList<>();
			metalist.add(sample.getId());
			metalist.add(sample.getLabel());
			for (String header : TEMPLATE) {
				if (data.containsKey(header)) {
					metalist.add(data.get(header));
				} else {
					metalist.add("");
				}
			}
			metadata.add(metalist);
		}
		model.addAttribute("metadata", metadata);
		return "projects/project_linelist";
	}
//
//	@RequestMapping("/metadata")
//	@ResponseBody
//	public DatatablesResponse<Map<String, Object>> getListListData(@PathVariable Long projectId,
//			@DatatablesParams DatatablesCriterias criterias) {
//		List<Project> projectList = ImmutableList.of(projectService.read(projectId));
//		List<String> sampleNames = ImmutableList.of();
//		ColumnDef sortedColumn = criterias.getSortedColumnDefs().get(0);
//
//		// Get the currently displayed page.
//		final Page<ProjectSampleJoin> page = sampleService.getFilteredSamplesForProjects(
//				projectList,
//				sampleNames,
//				"",
//				criterias.getSearch(),
//				null,
//				null,
//				DatatablesUtils.getCurrentPage(criterias),
//				criterias.getLength(),
//				DatatablesUtils.generateSortDirection(sortedColumn),
//				"sample.id"
//				);
//
//		List<Map<String, Object>> pagedMetadata = new ArrayList<>();
//		for (ProjectSampleJoin join : page) {
//			Sample sample = join.getObject();
//			SampleMetadata sampleMetadata = sampleService.getMetadataForSample(sample);
//			// TODO: This is where the project template should be applied.  For now return everything.
//			Map<String, Object> metadata;
//			if(sampleMetadata != null) {
//				metadata = sampleMetadata.getMetadata();
//			} else {
//				metadata = new HashMap();
//			}
//			metadata.put("identifier", sample.getId());
//			metadata.put("label", sample.getLabel());
//			pagedMetadata.add(metadata);
//		}
//
//		DataSet<Map<String, Object>> dataSet = new DataSet<>(pagedMetadata, page.getTotalElements(),
//				page.getTotalElements());
//		return DatatablesResponse.build(dataSet, criterias);
//	}
}
