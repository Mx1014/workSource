//
// EvhQualityInspectionTaskAttachmentDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityInspectionTaskAttachmentDTO
//
@interface EvhQualityInspectionTaskAttachmentDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* recordId;

@property(nonatomic, copy) NSString* contentType;

@property(nonatomic, copy) NSString* contentUri;

@property(nonatomic, copy) NSString* contentUrl;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSNumber* createTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

