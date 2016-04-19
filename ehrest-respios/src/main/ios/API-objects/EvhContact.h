//
// EvhContact.h
// generated at 2016-04-19 14:25:56 
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

