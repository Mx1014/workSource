//
// EvhParkEnterpriseActionData.h
// generated at 2016-03-28 15:56:07 
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

