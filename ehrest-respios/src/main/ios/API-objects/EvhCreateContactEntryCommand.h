//
// EvhCreateContactEntryCommand.h
// generated at 2016-04-12 19:00:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateContactEntryCommand
//
@interface EvhCreateContactEntryCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* contactId;

@property(nonatomic, copy) NSNumber* entryType;

@property(nonatomic, copy) NSString* entryValue;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

