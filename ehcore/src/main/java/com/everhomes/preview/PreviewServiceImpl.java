package com.everhomes.preview;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.everhomes.activity.ActivityRoster;
import com.everhomes.activity.ActivityServiceImpl;
import com.everhomes.forum.ForumProvider;
import com.everhomes.rest.activity.ActivityServiceErrorCode;
import com.everhomes.rest.preview.AddPreviewCommand;
import com.everhomes.rest.preview.PreviewDTO;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class PreviewServiceImpl implements PreviewService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PreviewServiceImpl.class);
	
	@Autowired
    private PreviewProvider previewProvider;
	
	@Override
	public PreviewDTO addPreview(AddPreviewCommand cmd) {
		Preview preview = ConvertHelper.convert(cmd, Preview.class);
		preview = this.previewProvider.addPreview(preview);
		
		PreviewDTO previewDTO = ConvertHelper.convert(preview, PreviewDTO.class);
		return previewDTO;
	}

	@Override
	public PreviewDTO getPreview(Long previewId) {
		Preview preview = this.previewProvider.getPreview(previewId);
		
		PreviewDTO previewDTO = ConvertHelper.convert(preview, PreviewDTO.class);
		return previewDTO;
	}


}
