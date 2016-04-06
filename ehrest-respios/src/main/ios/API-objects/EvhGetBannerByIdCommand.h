//
// EvhGetBannerByIdCommand.h
// generated at 2016-04-06 19:10:43 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetBannerByIdCommand
//
@interface EvhGetBannerByIdCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

