//
// EvhCreateBannerClickCommand.h
// generated at 2016-04-26 18:22:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateBannerClickCommand
//
@interface EvhCreateBannerClickCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* bannerId;

@property(nonatomic, copy) NSNumber* familyId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

