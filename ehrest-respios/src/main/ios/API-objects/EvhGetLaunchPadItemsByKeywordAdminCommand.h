//
// EvhGetLaunchPadItemsByKeywordAdminCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetLaunchPadItemsByKeywordAdminCommand
//
@interface EvhGetLaunchPadItemsByKeywordAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* keyword;

@property(nonatomic, copy) NSNumber* pageOffset;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

