//
// EvhParkEnterpriseActionData.h
// generated at 2016-04-01 15:40:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkEnterpriseActionData
//
@interface EvhParkEnterpriseActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* type;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

