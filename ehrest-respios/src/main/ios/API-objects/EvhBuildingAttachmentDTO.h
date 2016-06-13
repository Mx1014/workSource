//
// EvhBuildingAttachmentDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBuildingAttachmentDTO
//
@interface EvhBuildingAttachmentDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* buildingId;

@property(nonatomic, copy) NSString* contentType;

@property(nonatomic, copy) NSString* contentUri;

@property(nonatomic, copy) NSString* contentUrl;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

