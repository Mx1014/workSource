//
// EvhBuildingForRentAttachmentDTO.h
// generated at 2016-04-18 14:48:52 
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

