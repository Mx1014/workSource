//
// EvhBuildingForRentAttachmentDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBuildingForRentAttachmentDTO
//
@interface EvhBuildingForRentAttachmentDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* contentUrl;

@property(nonatomic, copy) NSString* contentUri;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

