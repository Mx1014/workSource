//
// EvhListEnterpriseVideoConfAccountCommand.h
// generated at 2016-04-07 15:16:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListEnterpriseVideoConfAccountCommand
//
@interface EvhListEnterpriseVideoConfAccountCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSString* keyword;

@property(nonatomic, copy) NSNumber* isAssigned;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

