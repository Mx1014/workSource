package com.everhomes.preview;

import com.everhomes.rest.preview.AddPreviewCommand;
import com.everhomes.rest.preview.PreviewDTO;

public interface PreviewService{

	PreviewDTO addPreview(AddPreviewCommand cmd);
	    
	PreviewDTO getPreview(Long previewId);

}
