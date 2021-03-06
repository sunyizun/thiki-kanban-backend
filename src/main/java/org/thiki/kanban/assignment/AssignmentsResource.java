package org.thiki.kanban.assignment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.thiki.kanban.card.CardsController;
import org.thiki.kanban.foundation.common.RestResource;
import org.thiki.kanban.foundation.hateoas.TLink;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by xubitao on 6/16/16.
 */
@Service
public class AssignmentsResource extends RestResource {
    public static Logger logger = LoggerFactory.getLogger(AssignmentsResource.class);
    @Resource
    private TLink tlink;
    @Resource
    private AssignmentResource assignmentResourceService;

    @Cacheable(value = "assignment", key = "'assignments'+#boardId+#stageId+#cardId+#userName")
    public Object toResource(List<Assignment> assignmentList, String boardId, String stageId, String cardId, String userName) throws Exception {
        logger.info("build assignments resource.boardId:{},stageId:{},cardId:{},userName:{}", boardId, stageId, cardId, userName);
        AssignmentsResource assignmentsResource = new AssignmentsResource();
        List<Object> assignmentResources = new ArrayList<>();
        for (Assignment assignment : assignmentList) {
            Object assignmentResource = assignmentResourceService.toResource(assignment, boardId, stageId, cardId, userName);
            assignmentResources.add(assignmentResource);
        }

        assignmentsResource.buildDataObject("assignments", assignmentResources);
        Link selfLink = linkTo(methodOn(AssignmentController.class).findByCardId(boardId, stageId, cardId, userName)).withSelfRel();
        assignmentsResource.add(tlink.from(selfLink).build(userName));

        Link cardLink = linkTo(methodOn(CardsController.class).findById(boardId, stageId, cardId, userName)).withRel("card");
        assignmentsResource.add(tlink.from(cardLink).build(userName));
        logger.info("assignments resource building completed.boardId:{},stageId:{},cardId:{},userName:{}", boardId, stageId, cardId, userName);
        return assignmentsResource.getResource();
    }
}
