// @formatter:off
package com.everhomes.enterprisemoment;

import com.everhomes.rest.enterprisemoment.*;
import com.everhomes.rest.comment.CommentDTO;
import com.everhomes.rest.enterprisemoment.CheckAdminCommand;
import com.everhomes.rest.enterprisemoment.CheckAdminResponse;
import com.everhomes.rest.enterprisemoment.CreateMomentCommand;
import com.everhomes.rest.enterprisemoment.DeleteMomentCommand;
import com.everhomes.rest.enterprisemoment.GetMomentDetailCommand;
import com.everhomes.rest.enterprisemoment.LikeMomentCommand;
import com.everhomes.rest.enterprisemoment.ListMomentFavouritesCommand;
import com.everhomes.rest.enterprisemoment.ListMomentFavouritesResponse;
import com.everhomes.rest.enterprisemoment.ListMomentMessagesCommand;
import com.everhomes.rest.enterprisemoment.ListMomentMessagesResponse;
import com.everhomes.rest.enterprisemoment.ListMomentsCommand;
import com.everhomes.rest.enterprisemoment.ListMomentsResponse;
import com.everhomes.rest.enterprisemoment.ListTagsCommand;
import com.everhomes.rest.enterprisemoment.ListTagsResponse;
import com.everhomes.rest.enterprisemoment.MomentDTO;
import com.everhomes.rest.enterprisemoment.UnlikeMomentCommand;

import java.util.List;

public interface EnterpriseMomentService {

    ListMomentsResponse listMoments(ListMomentsCommand cmd);

    ListTagsResponse listTags(ListTagsCommand cmd);

    ListTagsResponse editTags(EditTagsCommand cmd);

    MomentDTO createMoment(CreateMomentCommand cmd);

    void likeMoment(LikeMomentCommand cmd);

    void unlikeMoment(UnlikeMomentCommand cmd);

    ListMomentFavouritesResponse listMomentFavourites(ListMomentFavouritesCommand cmd);

    ListMomentMessagesResponse listMomentMessages(ListMomentMessagesCommand cmd);

    MomentDTO getMomentDetail(GetMomentDetailCommand cmd);

    void deleteMoment(DeleteMomentCommand cmd);

    CheckAdminResponse checkAdmin(CheckAdminCommand cmd);

    void incrNewMessageCount(Long userId, Long organizationId, int incr);

    void clearNewMessageCount(Long userId, Long organizationId);

    void createNewMessageAfterDoFavourite(EnterpriseMoment moment, EnterpriseMomentFavourite favourite);

    List<CommentDTO> buildCommentDTO(Integer namespaceId, Long organizationId, List<EnterpriseMomentComment> comments);

    GetBannerResponse getBanner(GetBannerCommand cmd);
}