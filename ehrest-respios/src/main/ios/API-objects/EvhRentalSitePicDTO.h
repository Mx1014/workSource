//
// EvhRentalSitePicDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalSitePicDTO
//
@interface EvhRentalSitePicDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* uri;

@property(nonatomic, copy) NSString* url;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

