//
// EvhCreateBannerClickCommand.h
// generated at 2016-03-25 11:43:32 
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

