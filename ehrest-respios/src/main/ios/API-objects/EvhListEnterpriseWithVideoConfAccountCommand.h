//
// EvhListEnterpriseWithVideoConfAccountCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListEnterpriseWithVideoConfAccountCommand
//
@interface EvhListEnterpriseWithVideoConfAccountCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* keyword;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

