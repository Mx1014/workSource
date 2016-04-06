//
// EvhFindLeasePromotionByIdCommand.h
// generated at 2016-04-06 19:59:44 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindLeasePromotionByIdCommand
//
@interface EvhFindLeasePromotionByIdCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

