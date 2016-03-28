//
// EvhCreateBannerClickCommand.h
// generated at 2016-03-25 19:05:20 
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

