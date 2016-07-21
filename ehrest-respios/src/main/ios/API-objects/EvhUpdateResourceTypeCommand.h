//
// EvhUpdateResourceTypeCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateResourceTypeCommand
//
@interface EvhUpdateResourceTypeCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSNumber* pageType;

@property(nonatomic, copy) NSString* iconUri;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* namespaceId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

