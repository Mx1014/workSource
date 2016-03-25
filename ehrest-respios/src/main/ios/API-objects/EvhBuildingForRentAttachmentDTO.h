//
// EvhBuildingForRentAttachmentDTO.h
// generated at 2016-03-25 15:57:21 
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

