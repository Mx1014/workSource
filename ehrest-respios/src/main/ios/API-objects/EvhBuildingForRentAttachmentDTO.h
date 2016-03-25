//
// EvhBuildingForRentAttachmentDTO.h
// generated at 2016-03-25 11:43:32 
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

