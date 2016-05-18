//
// EvhContact.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhContact
//
@interface EvhContact
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* contactPhone;

@property(nonatomic, copy) NSString* contactName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

