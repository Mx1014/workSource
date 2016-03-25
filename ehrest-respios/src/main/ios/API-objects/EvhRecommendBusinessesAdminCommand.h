//
// EvhRecommendBusinessesAdminCommand.h
// generated at 2016-03-25 11:43:34 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBusinessScope.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRecommendBusinessesAdminCommand
//
@interface EvhRecommendBusinessesAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* recommendStatus;

// item type EvhBusinessScope*
@property(nonatomic, strong) NSMutableArray* scopes;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

